package com.nespresso.filestorage.dto;

import java.util.UUID;

public record FileMetadataDto(UUID relatedObjectId,
                              String bucketName,
                              String fileName) { }
