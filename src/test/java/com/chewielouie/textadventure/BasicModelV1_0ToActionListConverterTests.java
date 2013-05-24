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

    private ModelLocation addMockLocationToOldModel( String id ) {
        ModelLocation loc = mock( ModelLocation.class );
        when( oldModel.findLocationByID( id ) ).thenReturn( loc );
        return loc;
    }

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

    private void addItemToOldModelInventory( String id ) {
        Item item = makeItemMock( id );
        when( oldModel.inventoryItems() )
            .thenReturn( new ArrayList<Item>( Arrays.asList( item ) ) );
    }

    private Item addItemToOldModel( String id ) {
        Item item = makeItemMock( id );
        when( oldModel.findItemByID( id ) ).thenReturn( item );
        return item;
    }

    private Item addItemToNewModel( String id ) {
        Item item = makeItemMock( id );
        when( newModel.findItemByID( id ) ).thenReturn( item );
        return item;
    }

    private BasicModelV1_0ToActionListConverter newConverter() {
        return new BasicModelV1_0ToActionListConverter( oldModel, newModel,
                                                        inventory, actionFactory );
    }

    @Test
    public void if_banana_peel_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "bananapeel" );
        Item newBananaPeel = addItemToNewModel( "bananapeel" );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newBananaPeel,
                                                              inventory,
                                                              townEntrance );
    }

    @Test
    public void if_dust_of_the_ancients_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "dustoftheancients" );
        Item newItem = addItemToNewModel( "dustoftheancients" );
        ModelLocation itemLocation = addMockLocationToNewModel( "mainstreettown" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_spade_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "spade" );
        Item newItem = addItemToNewModel( "spade" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_skeleton_key_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clocktowerskeletonkey" );
        Item newSkeletonKey = addItemToNewModel( "clocktowerskeletonkey" );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newSkeletonKey,
                                                              inventory,
                                                              townEntrance );
    }

    @Test
    public void if_locked_door_name_is_unlocked_add_use_key_and_door_action() {
        Item lockedDoor = addItemToOldModel( "lockeddoor" );
        when( lockedDoor.name() ).thenReturn( "unlocked door" );
        Item newLockedDoor = addItemToNewModel( "lockeddoor" );
        Item newSkeletonKey = addItemToNewModel( "clocktowerskeletonkey" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( newLockedDoor,
                                                                 newSkeletonKey );
    }

// Clock face lifetime
    @Test
    public void if_clock_face_is_on_floor_add_use_spade_and_mound_of_earth_action() {
        Item clockFace = addItemToOldModel( "clockface" );
        ModelLocation location = addMockLocationToOldModel( "townoutbuildings" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockFace ) ) );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( mound, spade );
    }

    @Test
    public void if_clock_face_has_been_picked_up_add_use_spade_and_mound_of_earth_action() {
        addItemToOldModelInventory( "clockface" );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( mound, spade );
    }

    @Test
    public void if_clock_face_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockface" );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_face_is_visible_add_use_spade_and_mound_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithface" );
        when( mechanism.visible() ).thenReturn( true );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( mound, spade );
    }

    @Test
    public void if_clock_mechanism_with_face_is_visible_add_take_face_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithface" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_face_is_visible_add_use_face_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithface" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanism" );
        Item clockFace = addItemToNewModel( "clockface" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( newMechanism, clockFace );
    }

// Clock hour hand lifetime
    @Test
    public void if_clock_hour_hand_is_on_shed_floor_add_examine_bags_of_junk_action() {
        Item clockHourHand = addItemToOldModel( "clockhourhand" );
        ModelLocation location = addMockLocationToOldModel( "smallshed" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockHourHand ) ) );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().actions();

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_hour_hand_has_been_picked_up_add_examine_bags_of_junk_action() {
        addItemToOldModelInventory( "clockhourhand" );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().actions();

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_hour_hand_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockhourhand" );
        Item newItem = addItemToNewModel( "clockhourhand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_examine_bags_of_junk_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().actions();

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_take_hour_hand_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockhourhand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_use_hour_hand_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanismwithface" );
        Item clockHourHand = addItemToNewModel( "clockhourhand" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( newMechanism,
                                                                 clockHourHand );
    }

// Clock minute hand lifetime
    @Test
    public void if_clock_minute_hand_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockminutehand" );
        Item newItem = addItemToNewModel( "clockminutehand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "clocktower" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_take_minute_hand_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockminutehand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "clocktower" );

        newConverter().actions();

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_use_minute_hand_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanismwithfaceandhourhand" );
        Item clockMinuteHand = addItemToNewModel( "clockminutehand" );

        newConverter().actions();

        verify( actionFactory ).createUseWithSpecificItemAction( newMechanism,
                                                                 clockMinuteHand );
    }

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

