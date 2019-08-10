package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryCommandToCategory;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final NotesCommandToNotes notesCommandToNotes;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryCommandToCategory,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 NotesCommandToNotes notesCommandToNotes) {
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.notesCommandToNotes = notesCommandToNotes;
    }


    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand command) {
        if (command == null) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(command.getId());
        recipe.setCookTime(command.getCookTime());
        recipe.setPrepTime(command.getPrepTime());
        recipe.setDescription(command.getDescription());
        recipe.setDifficulty(command.getDifficulty());
        recipe.setDirections(command.getDirections());
        recipe.setServings(command.getServings());
        recipe.setSource(command.getSource());
        recipe.setUrl(command.getUrl());

        recipe.setNotes(notesCommandToNotes.convert(command.getNotes()));

        if(command.getCategories() != null && command.getCategories().size() > 0){
            command.getCategories().forEach(categoryCommand ->
                    recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand)));
        }

        if(command.getIngredients() != null && command.getIngredients().size() > 0){
            command.getIngredients().forEach(ingredientCommand ->
                    recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredientCommand)));
        }

        return recipe;
    }
}
