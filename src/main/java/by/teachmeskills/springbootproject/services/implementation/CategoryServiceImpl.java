package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.converters.CategoryConverter;
import by.teachmeskills.springbootproject.repositories.implementation.CategoryRepositoryImpl;
import by.teachmeskills.springbootproject.services.CategoryService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepositoryImpl categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public void saveToFile(List<CategoryDto> categories) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = Files.newBufferedWriter(Paths.get("src/main/resources/categories.csv"))) {
            StatefulBeanToCsv<CategoryDto> beanToCsv = new StatefulBeanToCsvBuilder<CategoryDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            beanToCsv.write(categories);
        }
    }

    @Override
    @Transactional
    public List<CategoryDto> loadFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<CategoryDto> csvToBean = new CsvToBeanBuilder<CategoryDto>(reader)
                    .withType(CategoryDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator('~')
                    .build();
            List<CategoryDto> categories = new ArrayList<>();
            List<CategoryDto> result = new ArrayList<>();
            csvToBean.forEach(categories::add);
            categories.stream().map(categoryConverter::fromDto).forEach(c -> {
                categoryRepository.create(c);
                result.add(categoryConverter.toDto(c));
            });
            return result;
        }
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryDto category) {
        return categoryConverter.toDto(categoryRepository.create(categoryConverter.fromDto(category)));
    }

    @Override
    public List<CategoryDto> read() {
        return categoryRepository.read().stream().map(categoryConverter::toDto).toList();
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto category) {
        return categoryConverter.toDto(categoryRepository.update(categoryConverter.fromDto(category)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
