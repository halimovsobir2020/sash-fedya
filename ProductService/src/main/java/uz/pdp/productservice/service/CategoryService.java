package uz.pdp.productservice.service;

import org.springframework.stereotype.Service;
import uz.pdp.productservice.dto.CategoryDTO;
import uz.pdp.productservice.entity.Category;
import uz.pdp.productservice.repo.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(CategoryDTO dto) {
        return repository.save(new Category(dto.getName()));
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Category getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category update(Long id, CategoryDTO dto) {
        Category category = getById(id);
        category.setName(dto.getName());
        return repository.save(category);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
