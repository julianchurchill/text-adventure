package com.chewielouie.textadventure2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.chewielouie.textadventure_common.TextAdventureCommonActivity;
import com.chewielouie.textadventure.BasicModelFactory;
import com.chewielouie.textadventure.RendersView;
import com.chewielouie.textadventure.UserActionHandler;

public class TextAdventureActivity extends TextAdventureCommonActivity {

    public TextAdventureActivity() {
        super();
    }

    public TextAdventureActivity( RendersView r ) {
        super( r );
    }

    public TextAdventureActivity( BasicModelFactory modelFactory ) {
        super( modelFactory );
    }

    public TextAdventureActivity( RendersView r, BasicModelFactory modelFactory ) {
        super( r, modelFactory );
    }

    public TextAdventureActivity( UserActionHandler u ) {
        super( u );
    }

    protected Bitmap getMap() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.map);
    }

    protected int R_drawable_map() {
        return R.drawable.map;
    }

    protected int R_drawable_tta_button() {
        return R.drawable.tta_button;
    }

    protected int R_id_available_actions() {
        return R.id.available_actions;
    }

    protected int R_id_location_text_view() {
        return R.id.location_text_view;
    }

    protected int R_id_map_view() {
        return R.id.map_view;
    }

    protected int R_id_main_text_output() {
        return R.id.main_text_output;
    }

    protected int R_id_main_text_output_scroll_view() {
        return R.id.main_text_output_scroll_view;
    }

    protected int R_id_options_font_example_text() {
        return R.id.options_font_example_text;
    }

    protected int R_id_options_font_size_picker() {
        return R.id.options_font_size_picker;
    }

    protected int R_id_options_tts_enabled() {
        return R.id.options_tts_enabled;
    }

    protected int R_id_score_text_view() {
        return R.id.score_text_view;
    }

    protected int R_layout_about_dialog() {
        return R.layout.about_dialog;
    }

    protected int R_layout_main() {
        return R.layout.main;
    }

    protected int R_layout_options_dialog() {
        return R.layout.options_dialog;
    }

    protected int R_raw_model_content() {
        return R.raw.model_content;
    }

    protected int R_string_about() {
        return R.string.about;
    }

    protected int R_string_app_name() {
        return R.string.app_name;
    }

    protected int R_string_completed() {
        return R.string.completed;
    }

    protected int R_string_new_game() {
        return R.string.new_game;
    }

    protected int R_string_new_game_title() {
        return R.string.new_game_title;
    }

    protected int R_string_new_game_confirmation_dialog_text() {
        return R.string.new_game_confirmation_dialog_text;
    }

    protected int R_string_no() {
        return R.string.no;
    }

    protected int R_string_options() {
        return R.string.options;
    }

    protected int R_string_options_title() {
        return R.string.options_title;
    }

    protected int R_string_options_save() {
        return R.string.options_save;
    }

    protected int R_string_options_cancel() {
        return R.string.options_cancel;
    }

    protected int R_string_show_map() {
        return R.string.show_map;
    }

    protected int R_string_yes() {
        return R.string.yes;
    }

}
