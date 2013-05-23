package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BasicModelV1_0ToActionListConverterTests {
    @Mock
    UserInventory inventory;

    @Mock
    TextAdventureModel oldModel;

    @Mock
    TextAdventureModel newModel;

    @Mock
    ActionFactory actionFactory;

    private ModelLocation addMockLocationToNewModel( String id ) {
        ModelLocation loc = mock( ModelLocation.class );
        when( newModel.findLocationByID( id ) ).thenReturn( loc );
        return loc;
    }

    private Item makeItemMock( String id ) {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( id );
        return item;
    }

    private BasicModelV1_0ToActionListConverter newConverter() {
        return new BasicModelV1_0ToActionListConverter( oldModel, newModel,
                                                        inventory, actionFactory );
    }

    @Test
    public void if_banana_peel_has_been_picked_up_add_take_action() {
        Item oldBananaPeel = makeItemMock( "bananapeel" );
        when( oldModel.inventoryItems() )
            .thenReturn( new ArrayList<Item>( Arrays.asList( oldBananaPeel ) ) );
        Item newBananaPeel = makeItemMock( "bananapeel" );
        when( newModel.findItemByID( "bananapeel" ) ).thenReturn( newBananaPeel );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newBananaPeel,
                                                              inventory,
                                                              townEntrance );
    }

    @Test
    public void if_dust_of_the_ancients_has_been_picked_up_add_take_action() {
        Item oldItem = makeItemMock( "dustoftheancients" );
        when( oldModel.inventoryItems() )
            .thenReturn( new ArrayList<Item>( Arrays.asList( oldItem ) ) );
        Item newItem = makeItemMock( "dustoftheancients" );
        when( newModel.findItemByID( "dustoftheancients" ) ).thenReturn( newItem );
        ModelLocation itemLocation = addMockLocationToNewModel( "mainstreettown" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_spade_has_been_picked_up_add_take_action() {
        Item oldItem = makeItemMock( "spade" );
        when( oldModel.inventoryItems() )
            .thenReturn( new ArrayList<Item>( Arrays.asList( oldItem ) ) );
        Item newItem = makeItemMock( "spade" );
        when( newModel.findItemByID( "spade" ) ).thenReturn( newItem );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_skeleton_key_has_been_picked_up_add_take_action() {
        Item oldSkeletonKey = makeItemMock( "clocktowerskeletonkey" );
        when( oldModel.inventoryItems() )
            .thenReturn( new ArrayList<Item>( Arrays.asList( oldSkeletonKey ) ) );
        Item newSkeletonKey = makeItemMock( "clocktowerskeletonkey" );
        when( newModel.findItemByID( "clocktowerskeletonkey" ) )
            .thenReturn( newSkeletonKey );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newSkeletonKey,
                                                              inventory,
                                                              townEntrance );
    }

    @Test
    public void if_locked_door_name_is_unlocked_add_use_key_and_door_action() {
        Item lockedDoor = makeItemMock( "lockeddoor" );
        when( lockedDoor.name() ).thenReturn( "unlocked door" );
        when( oldModel.findItemByID( "lockeddoor" ) ).thenReturn( lockedDoor );
        Item newLockedDoor = makeItemMock( "lockeddoor" );
        when( newModel.findItemByID( "lockeddoor" ) ).thenReturn( newLockedDoor );
        Item newSkeletonKey = makeItemMock( "clocktowerskeletonkey" );
        when( newModel.findItemByID( "clocktowerskeletonkey" ) )
            .thenReturn( newSkeletonKey );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( newLockedDoor,
                                                                 newSkeletonKey );
    }

// Clock face lifetime
    // @Test
    // public void if_clock_face_is_on_floor_add_use_spade_and_mound_of_earth_action() {

    // @Test
    // public void if_clock_face_has_been_picked_up_add_use_spade_and_mound_of_earth_action() {

    // @Test
    // public void if_clock_face_has_been_picked_up_add_take_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__has_an_engraved_face__add_use_spade_and_mound_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__has_an_engraved_face__add_take_face_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__has_an_engraved_face__add_use_face_and_mechanism_action() {

// Clock hour hand lifetime
    // @Test
    // public void if_clock_hour_hand_is_on_shed_floor_add_examine_bags_of_junk_action() {

    // @Test
    // public void if_clock_hour_hand_has_been_picked_up_add_examine_bags_of_junk_action() {

    // @Test
    // public void if_clock_hour_hand_has_been_picked_up_add_take_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__missing_its_minute_hand__add_examine_bags_of_junk_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__missing_its_minute_hand__add_take_hour_hand_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__missing_its_minute_hand__add_use_hour_hand_and_mechanism_action() {

// Clock minute hand lifetime
    // @Test
    // public void if_clock_minute_hand_has_been_picked_up_add_take_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__and_both_hands__add_take_minute_hand_action() {

    // @Test
    // public void if_clock_mechanism_description_includes__and_both_hands__add_use_minute_hand_and_mechanism_action() {

////// End of clock face story //////
////// Start of mine rescue story //////

// Axe head lifetime
    // @Test
    // public void if_axe_head_is_on_floor_of_annex_add_examine_chunk_of_metal_action() {

// Wooden pole lifetime
    // @Test
    // public void if_wooden_pole_is_on_floor_of_smaller_annex_add_examine_straw_action() {

    // @Test
    // public void if_wooden_pole_has_been_picked_up_add_take_action() {

// Blunt pick axe lifetime
    // @Test
    // public void if_blunt_pick_axe_is_on_floor_of_annex_add_examine_chunk_of_metal_action() {

    // @Test
    // public void if_blunt_pick_axe_is_on_floor_of_annex_add_examine_straw_action() {

    // @Test
    // public void if_blunt_pick_axe_is_on_floor_of_annex_add_take_wooden_pole_action() {

    // @Test
    // public void if_blunt_pick_axe_is_on_floor_of_annex_add_use_wooden_pole_and_axe_head_action() {

    // @Test
    // public void if_blunt_pick_axe_has_been_picked_up_add_examine_chunk_of_metal_action() {

    // @Test
    // public void if_blunt_pick_axe_has_been_picked_up_add_examine_straw_action() {

    // @Test
    // public void if_blunt_pick_axe_has_been_picked_up_add_take_wooden_pole_action() {

    // @Test
    // public void if_blunt_pick_axe_has_been_picked_up_add_use_wooden_pole_and_axe_head_action() {

    // @Test
    // public void if_blunt_pick_axe_has_been_picked_up_add_take_action() {

// Pick axe lifetime
    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_examine_chunk_of_metal_action() {

    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_examine_straw_action() {

    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_take_wooden_pole_action() {

    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_use_wooden_pole_and_axe_head_action() {

    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_take_blunt_axe_action() {

    // @Test
    // public void if_pick_axe_is_on_floor_of_smithy_add_use_blunt_pick_axe_and_wheel_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_examine_chunk_of_metal_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_examine_straw_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_take_wooden_pole_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_use_wooden_pole_and_axe_head_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_take_blunt_axe_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_use_blunt_pick_axe_and_wheel_action() {

    // @Test
    // public void if_pick_axe_has_been_picked_up_add_take_pick_axe_action() {

// Map lifetime
    // @Test
    // public void if_map_is_on_floor_of_chamber_add_examine_table_action() {

    // @Test
    // public void if_map_has_been_picked_up_add_examine_table_action() {

    // @Test
    // public void if_map_has_been_picked_up_add_take_action() {

// Shopkeeper lifetime
    // @Test
    // public void if_shop_keeper_is_in_cave_add_use_pick_axe_with_rock_action() {

    // @Test
    // public void if_ruby_count_is_2_add_use_pick_axe_with_rock_action() {

    // @Test
    // public void if_ruby_count_is_2_add_use_map_with_shop_keeper_action() {
}

