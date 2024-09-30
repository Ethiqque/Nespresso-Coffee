package com.nespresso.user.endpoint;

import com.nespresso.email.api.EmailTokenConformer;
import com.nespresso.email.api.EmailTokenSender;
import com.nespresso.openapi.dto.*;
import com.nespresso.security.api.SecurityPrincipalProvider;
import com.nespresso.user.api.ChangeUserPasswordOperationPerformer;
import com.nespresso.user.api.DeleteUserOperationPerformer;
import com.nespresso.user.api.SingleUserProvider;
import com.nespresso.user.api.UpdateUserOperationPerformer;
import com.nespresso.filestorage.file.FileDeleter;
import com.nespresso.user.api.avatar.UserAvatarLinkProvider;
import com.nespresso.user.api.avatar.UserAvatarUploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = UserEndpoint.API_CUSTOMERS)
public class UserEndpoint implements UserApi {

    public static final String API_CUSTOMERS = "/api/v1/users";

    private final UpdateUserOperationPerformer updateUserOperationPerformer;
    private final SingleUserProvider singleUserProvider;
    private final ChangeUserPasswordOperationPerformer changeUserPasswordOperationPerformer;
    private final DeleteUserOperationPerformer deleteUserOperationPerformer;
    private final SecurityPrincipalProvider securityPrincipalProvider;
    private final UserAvatarUploader userAvatarUploader;
    private final FileDeleter fileDeleter;
    private final UserAvatarLinkProvider userAvatarLinkProvider;
    private final EmailTokenSender emailTokenSender;
    private final EmailTokenConformer emailTokenConformer;

    @Override
    @GetMapping
    public ResponseEntity<UserDto> getUserProfile() {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to get the user with userId - {}.", userId);
        UserDto userDto = singleUserProvider.getUserById(userId);
        log.info("The request to retrieve the user's account was handled.");
        return ResponseEntity.ok()
                .body(userDto);
    }

    @Override
    @PutMapping
    public ResponseEntity<UserDto> editUserProfile(@Valid @RequestBody UpdateUserAccountRequest updateUserAccountRequest) {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to edit the User with userId - {}.", userId);
        UserDto updatedUserDto = updateUserOperationPerformer.updateUser(updateUserAccountRequest);
        log.info("The request to update the user's account was handled.");
        return ResponseEntity.ok()
                .body(updatedUserDto);
    }

    @Override
    @PatchMapping
    public ResponseEntity<Void> changeUserPassword(ChangeUserPasswordRequest changeUserPasswordRequest) {
        log.info("Received the request to change the user's password.");
        changeUserPasswordOperationPerformer.changeUserPassword(changeUserPasswordRequest);
        log.info("The request to change the user's password was handled.");
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile() {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to delete the user's account.");
        deleteUserOperationPerformer.deleteUser(userId);
        log.info("The request to delete the user's account was handled.");
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @PostMapping(path = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> uploadUserAvatar(@Validated @RequestParam(value = "file") MultipartFile file) {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to upload the user avatar.");
        userAvatarUploader.uploadUserAvatar(userId, file);
        log.info("The request to upload the user's avatar was handled.");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @GetMapping(path = "/avatar")
    public ResponseEntity<String> getUserAvatarLink() {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to get the user avatar link.");
        String userAvatar = userAvatarLinkProvider.getLink(userId);
        log.info("The request to retrieve the user's avatar link was handled.");
        return ResponseEntity.ok().body(userAvatar);
    }

    @Override
    @DeleteMapping(path = "/avatar")
    public ResponseEntity<Void> deleteUserAvatar() {
        UUID userId = securityPrincipalProvider.getUserId();
        log.info("Received the request to delete the user avatar.");
        fileDeleter.delete(userId);
        log.info("The request to delete the user avatar was handled.");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PostMapping(path = "/password/reset")
    public ResponseEntity<Void> resetUserPassword() {
        UserDto userDto = securityPrincipalProvider.get();
        log.info("Received the request to reset password for user with id: {}", userDto.getId());
        UserRegistrationRequest request = new UserRegistrationRequest(userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), "");
        emailTokenSender.sendEmailVerificationCode(request);
        log.info("The request to reset the user's password was handled.");
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @PostMapping(path = "/password/reset/confirm")
    public ResponseEntity<Void> confirmResetUserPassword(@RequestBody final ConfirmEmailRequest confirmEmailRequest) {
        log.info("Received email confirmation request to reset password");
        emailTokenConformer.confirmResetPasswordEmailByCode(new ConfirmEmailRequest(confirmEmailRequest.getToken()));
        log.info("Email verification to reset password completed");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
