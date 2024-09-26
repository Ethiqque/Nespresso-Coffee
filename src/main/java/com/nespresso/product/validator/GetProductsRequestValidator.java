package com.nespresso.product.validator;

import com.nespresso.common.validation.pagination.PaginationParametersValidator;
import com.nespresso.product.exception.GetProductsBadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetProductsRequestValidator {

    private static final Set<String> ALLOWED_SORT_ATTRIBUTES_VALUES = Set.of("name", "price", "quantity", "averageRating", "reviewsCount", "brandName", "sellerName");
    private static final Set<Integer> ALLOWED_MINIMUM_AVERAGE_RATING_VALUES = Set.of(1, 2, 3, 4);

    private final PaginationParametersValidator paginationParametersValidator;

    public void validate(final Integer pageNumber,
                         final Integer pageSize,
                         final String sortAttribute,
                         final String sortDirection,
                         final BigDecimal minPrice,
                         final BigDecimal maxPrice,
                         final Integer minimumAverageRating,
                         final List<String> brandNames,
                         final List<String> sellersNames) {
        StringBuilder errorMessages = new StringBuilder();

        StringBuilder paginationErrorMessages = paginationParametersValidator.validate(pageNumber, pageSize, sortAttribute, sortDirection, ALLOWED_SORT_ATTRIBUTES_VALUES);
        errorMessages.append(paginationErrorMessages);

        StringBuilder minMaxPriceParameterMessages = validateMinMaxPriceParameter(minPrice, maxPrice);
        errorMessages.append(minMaxPriceParameterMessages);

        StringBuilder getBrandNameListParameterErrorMessages = validateBrandNameList(brandNames);
        errorMessages.append(getBrandNameListParameterErrorMessages);

        StringBuilder getSellerNameListParameterErrorMessages = validateSellerNameList(sellersNames);
        errorMessages.append(getSellerNameListParameterErrorMessages);

        StringBuilder minimumAverageRatingParameterErrorMessages = validateMinimumAverageRatingParameter(minimumAverageRating);
        errorMessages.append(minimumAverageRatingParameterErrorMessages);

        if (!errorMessages.isEmpty()) {
            throw new GetProductsBadRequestException(errorMessages.toString());
        }
    }

    private StringBuilder validateMinimumAverageRatingParameter(final Integer minimumAverageRating) {
        StringBuilder errorMessages = new StringBuilder();

        if (minimumAverageRating != null && !ALLOWED_MINIMUM_AVERAGE_RATING_VALUES.contains(minimumAverageRating)) {
            String errorMessage = String.format("'%s' is incorrect 'minimumAverageRating' value. Allowed 'minimumAverageRating' values are '%s'.",
                            minimumAverageRating, ALLOWED_MINIMUM_AVERAGE_RATING_VALUES);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        return errorMessages;
    }

    private static StringBuilder validateMinMaxPriceParameter(BigDecimal minPrice, BigDecimal maxPrice) {
        final StringBuilder errorMessages = new StringBuilder();

        if (minPrice != null && minPrice.signum() < 0) {
            String errorMessage = String.format("'%s' is incorrect 'minPrice' value. 'MinPrice' value should be non negative integer or decimal number value.", minPrice);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        if (maxPrice != null && maxPrice.signum() < 0) {
            String errorMessage = String.format("'%s' is incorrect 'maxPrice' value. 'MaxPrice' value should be non negative integer or decimal number value.", maxPrice);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            String errorMessage = String.format("'%s' and '%s' are incorrect 'minPrice' and 'maxPrice' values. 'MaxPrice' value should be bigger than 'MinPrice' value.", minPrice, maxPrice);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        return errorMessages;
    }

    private static StringBuilder validateBrandNameList(List<String> brandNames) {
        final StringBuilder errorMessages = new StringBuilder();
        if ((brandNames != null && brandNames.stream().anyMatch(StringUtils::isAllBlank))) {
            String errorMessage = String.format("Some values of 'brandNames' list = '%s' are null. The list 'brandNames' must have no null values.",
                    brandNames);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        if ((brandNames != null && brandNames.stream().distinct().count() < brandNames.size())) {
            String errorMessage = String.format("The 'brandNames' list = '%s' has duplicates. The 'brandNames' list values must be unique.",
                    brandNames);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        return errorMessages;
    }

    private static StringBuilder validateSellerNameList(List<String> sellerNames) {
        final StringBuilder errorMessages = new StringBuilder();
        if ((sellerNames != null && sellerNames.stream().anyMatch(StringUtils::isAllBlank))) {
            String errorMessage = String.format("Some values of 'sellerNames' list = '%s' are null. The list 'sellerNames' must have no null values.",
                    sellerNames);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        if ((sellerNames != null && sellerNames.stream().distinct().count() < sellerNames.size())) {
            String errorMessage = String.format("The 'sellerNames' list = '%s' has duplicates. The 'sellerNames' list values must be unique.",
                    sellerNames);
            errorMessages.append(createErrorMessage(errorMessage));
        }
        return errorMessages;
    }

    private static String createErrorMessage(String errorMessage) {
        return String.format(" Error: { %s }. ", errorMessage);
    }
}
