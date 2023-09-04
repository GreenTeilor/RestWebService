package by.teachmeskills.springbootproject.services;

import by.teachmeskills.springbootproject.dto.CategoryDto;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService extends BaseService<CategoryDto>{
    void saveToFile(List<CategoryDto> categories, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
    List<CategoryDto> loadFromFile(MultipartFile file) throws IOException;
}
