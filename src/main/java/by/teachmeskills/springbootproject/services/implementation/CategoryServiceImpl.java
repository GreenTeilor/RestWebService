package by.teachmeskills.springbootproject.services.implementation;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import by.teachmeskills.springbootproject.dto.PagingParamsDto;
import by.teachmeskills.springbootproject.dto.converters.CategoryConverter;
import by.teachmeskills.springbootproject.exceptions.NoResourceFoundException;
import by.teachmeskills.springbootproject.repositories.CategoryRepository;
import by.teachmeskills.springbootproject.services.CategoryService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public void saveToFile(List<CategoryDto> categories, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<CategoryDto> beanToCsv = new StatefulBeanToCsvBuilder<CategoryDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "categories.csv");
            categories.forEach(c -> c.setId(null));
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
                categoryRepository.save(c);
                result.add(categoryConverter.toDto(c));
            });
            return result;
        }
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryDto category) {
        return categoryConverter.toDto(categoryRepository.save(categoryConverter.fromDto(category)));
    }

    @Override
    public List<CategoryDto> read(PagingParamsDto params) {
        Pageable paging = PageRequest.of(params.getPageNumber(), params.getPageSize(), Sort.by("name").ascending());
        return categoryRepository.findAll(paging).stream().map(categoryConverter::toDto).toList();
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto category) throws NoResourceFoundException {
        categoryRepository.findById(category.getId()).orElseThrow(() ->
                new NoResourceFoundException("No category with id " + category.getId() + " found"));
        return categoryConverter.toDto(categoryRepository.save(categoryConverter.fromDto(category)));
    }

    @Override
    @Transactional
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
