package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.ImageDto;
import by.teachmeskills.springbootproject.entities.Image;

import java.util.Optional;

public class ImageConverter implements Converter<ImageDto, Image> {
    @Override
    public ImageDto toDto(Image image) {
        return Optional.ofNullable(image).map(i -> ImageDto.builder().
                        id(i.getId()).
                        imagePath(i.getImagePath()).
                        categoryId(i.getCategoryId()).
                        productId(i.getProductId()).
                        primary(i.isPrimary()).
                        build()).
                orElse(null);
    }

    @Override
    public Image fromDto(ImageDto imageDto) {
        return Optional.ofNullable(imageDto).map(i -> Image.builder().
                        id(i.getId()).
                        imagePath(i.getImagePath()).
                        categoryId(i.getCategoryId()).
                        productId(i.getProductId()).
                        primary(i.isPrimary()).
                        build()).
                orElse(null);
    }
}
