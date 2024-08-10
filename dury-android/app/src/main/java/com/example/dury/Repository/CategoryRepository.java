package com.example.dury.Repository;

import androidx.lifecycle.MutableLiveData;

import com.example.dury.Model.Category;
import com.example.dury.Model.User;
import com.example.dury.Network.RetrofitClient;
import com.example.dury.Service.CategoryService;
import com.example.dury.ViewModel.IApiCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private final CategoryService categoryService;


    private MutableLiveData<Category> categoryLiveData = new MutableLiveData<>();

    public MutableLiveData<Category> getCategoryLiveData() {
        return categoryLiveData;
    }
    public CategoryRepository() {
        categoryService = RetrofitClient.getClient().create(CategoryService.class);
    }

    public void loadAllCategories(final ICategoryCallback callBack, String userId, String token) {
        Call<List<Category>> call = categoryService.loadAllByUser(userId, token);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new Throwable("Failed to load categories"));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }
    public void loadAllCategoriesByUid(String uid, String token, IApiCallback iApiCallback) {
        Call<List<Category>> call = categoryService.loadAllByUser(uid, token);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    iApiCallback.onSuccess(response.body());
                } else {
                    iApiCallback.onFailure(new Throwable("Failed to load categories"));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                iApiCallback.onFailure(throwable);
            }
        });

    }


    public void getCategory(String id, String token) {
        Call<Category> call = categoryService.getCategory(id, token);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    categoryLiveData.setValue(response.body());
                } else {
                    // Xử lý khi lấy danh mục thất bại
                    // Ví dụ: categoryLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                // Ví dụ: categoryLiveData.setValue(null);
            }
        });
    }

    // Phương thức để thêm danh mục
    public void addCategory(Category category, String token, IApiCallback callback) {
        Call<Category> call = categoryService.addCategory(category, token);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Failed to add category"));
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Phương thức để cập nhật danh mục
    public void updateCategory(Category category, String token, IApiCallback callback) {
        Call<Category> call = categoryService.updateCategory(category, token);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Failed to update category"));
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Phương thức để xóa danh mục
    public void deleteCategory(String id, String token) {
        Call<Void> call = categoryService.deleteCategory(id, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Người dùng được thêm thành công
                    System.out.println("Category deleted: " + response.body());
                } else {
                    // Xử lý lỗi, người dùng không được thêm
                    System.out.println("Failed to delete category");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }

    public void delete(Category category, String token, IApiCallback iApiCallback) {
        Call<Void> call = categoryService.deleteCategory(category.getId(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    iApiCallback.onSuccess(response.body());
                } else {
                    iApiCallback.onFailure(new Throwable("Failed to delete category"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                iApiCallback.onFailure(throwable);
            }
        });
    }
}
