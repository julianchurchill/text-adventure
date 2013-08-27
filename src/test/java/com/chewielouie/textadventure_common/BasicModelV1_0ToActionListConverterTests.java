package com.chewielouie.textadventure_common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        return new BasicModelV1_0ToActionListConverter( newModel,
                                                        inventory, actionFactory );
    }

    @Test
    public void if_banana_peel_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "bananapeel" );
        Item newBananaPeel = addItemToNewModel( "bananapeel" );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newBananaPeel,
                                                              inventory,
                                                              townEntrance );
    }

    @Test
    public void if_dust_of_the_ancients_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "dustoftheancients" );
        Item newItem = addItemToNewModel( "dustoftheancients" );
        ModelLocation itemLocation = addMockLocationToNewModel( "mainstreettown" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_spade_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "spade" );
        Item newItem = addItemToNewModel( "spade" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_skeleton_key_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clocktowerskeletonkey" );
        Item newSkeletonKey = addItemToNewModel( "clocktowerskeletonkey" );
        ModelLocation townEntrance = addMockLocationToNewModel( "townentrance" );

        newConverter().inferActionsFrom( oldModel );

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

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( newSkeletonKey,
                                                                 newLockedDoor );
    }

// Clock face lifetime
    @Test
    public void if_clock_face_is_on_floor_add_use_spade_and_mound_of_earth_action() {
        Item clockFace = addItemToOldModel( "clockface" );
        when( clockFace.visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "townoutbuildings" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockFace ) ) );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( spade, mound );
    }

    @Test
    public void if_clock_face_is_not_visible_do_not_add_use_spade_and_mound_of_earth_action() {
        Item clockFace = addItemToOldModel( "clockface" );
        when( clockFace.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "townoutbuildings" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockFace ) ) );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createUseWithSpecificItemAction(
                                                Mockito.any( Item.class ),
                                                Mockito.any( Item.class ) );
    }

    @Test
    public void if_clock_face_has_been_picked_up_add_use_spade_and_mound_of_earth_action() {
        addItemToOldModelInventory( "clockface" );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( spade, mound );
    }

    @Test
    public void if_clock_face_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockface" );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().inferActionsFrom( oldModel );

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

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( spade, mound );
    }

    @Test
    public void if_clock_mechanism_with_face_is_visible_add_take_face_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithface" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().inferActionsFrom( oldModel );

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

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockFace, newMechanism );
    }

// Clock hour hand lifetime
    @Test
    public void if_clock_hour_hand_is_on_shed_floor_add_examine_bags_of_junk_action() {
        Item clockHourHand = addItemToOldModel( "clockhourhand" );
        when( clockHourHand.visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "smallshed" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockHourHand ) ) );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_hour_hand_is_not_visible_do_not_add_examine_bags_of_junk_action() {
        Item clockHourHand = addItemToOldModel( "clockhourhand" );
        when( clockHourHand.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "smallshed" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( clockHourHand ) ) );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( Mockito.any( Item.class ) );
    }

    @Test
    public void if_clock_hour_hand_has_been_picked_up_add_examine_bags_of_junk_action() {
        addItemToOldModelInventory( "clockhourhand" );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_hour_hand_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockhourhand" );
        Item newItem = addItemToNewModel( "clockhourhand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_examine_bags_of_junk_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_take_hour_hand_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockhourhand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_use_spade_and_mound_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( spade, mound );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_take_face_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_use_face_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanism" );
        Item clockFace = addItemToNewModel( "clockface" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockFace, newMechanism );
    }

    @Test
    public void if_clock_mechanism_with_hour_hand_is_visible_add_use_hour_hand_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanismwithface" );
        Item clockHourHand = addItemToNewModel( "clockhourhand" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockHourHand,
                                                                 newMechanism );
    }

// Clock minute hand lifetime
    @Test
    public void if_clock_minute_hand_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "clockminutehand" );
        Item newItem = addItemToNewModel( "clockminutehand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "clocktower" );

        newConverter().inferActionsFrom( oldModel );

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

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_examine_bags_of_junk_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item bagsOfJunk = addItemToNewModel( "bagsofjunk" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( bagsOfJunk );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_take_hour_hand_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockhourhand" );
        ModelLocation itemLocation = addMockLocationToNewModel( "smallshed" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_use_spade_and_mound_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item mound = addItemToNewModel( "moundofearth" );
        Item spade = addItemToNewModel( "spade" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( spade, mound );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_take_face_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newItem = addItemToNewModel( "clockface" );
        ModelLocation itemLocation = addMockLocationToNewModel( "townoutbuildings" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_use_face_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanism" );
        Item clockFace = addItemToNewModel( "clockface" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockFace, newMechanism );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_use_hour_hand_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanismwithface" );
        Item clockHourHand = addItemToNewModel( "clockhourhand" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockHourHand,
                                                                 newMechanism );
    }

    @Test
    public void if_clock_mechanism_with_minute_hand_is_visible_add_use_minute_hand_and_mechanism_action() {
        Item mechanism = addItemToOldModel( "clockmechanismwithfaceandhourhandandminutehand" );
        when( mechanism.visible() ).thenReturn( true );
        Item newMechanism = addItemToNewModel( "clockmechanismwithfaceandhourhand" );
        Item clockMinuteHand = addItemToNewModel( "clockminutehand" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( clockMinuteHand,
                                                                 newMechanism );
    }

////// End of clock face story //////
////// Start of mine rescue story //////

// Axe head lifetime
    @Test
    public void if_axe_head_name_has_changed_add_examine_chunk_of_metal_action() {
        Item axeHead = addItemToOldModel( "axehead" );
        when( axeHead.name() ).thenReturn( "Axe head" );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( axeHead ) ) );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( newAxeHead );
    }

    @Test
    public void if_axe_head_name_has_not_changed_do_not_add_examine_chunk_of_metal_action() {
        Item axeHead = addItemToOldModel( "axehead" );
        when( axeHead.name() ).thenReturn( "Chunk of metal" );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( axeHead ) ) );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( newAxeHead );
    }

// Wooden pole lifetime
    @Test
    public void if_wooden_pole_is_on_floor_of_smaller_annex_add_examine_straw_action() {
        Item woodenPole = addItemToOldModel( "woodenpole" );
        when( woodenPole.visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "evensmallerannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( woodenPole ) ) );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }

    @Test
    public void if_wooden_pole_is_not_visible_do_not_add_examine_straw_action() {
        Item woodenPole = addItemToOldModel( "woodenpole" );
        when( woodenPole.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "evensmallerannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( woodenPole ) ) );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( Mockito.any( Item.class ) );
    }

    @Test
    public void if_wooden_pole_has_been_picked_up_add_examine_straw_action() {
        addItemToOldModelInventory( "woodenpole" );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }

    @Test
    public void if_wooden_pole_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "woodenpole" );
        Item newItem = addItemToNewModel( "woodenpole" );
        ModelLocation itemLocation = addMockLocationToNewModel( "evensmallerannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

// Blunt pick axe lifetime
    @Test
    public void if_blunt_pick_axe_is_on_floor_of_annex_add_examine_chunk_of_metal_action() {
        Item bluntPickAxe = addItemToOldModel( "bluntpickaxe" );
        when( bluntPickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( bluntPickAxe ) ) );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( newAxeHead );
    }

    @Test
    public void if_blunt_pick_axe_is_on_floor_of_annex_add_examine_straw_action() {
        Item bluntPickAxe = addItemToOldModel( "bluntpickaxe" );
        when( bluntPickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( bluntPickAxe ) ) );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }

    @Test
    public void if_blunt_pick_axe_is_on_floor_of_annex_add_take_wooden_pole_action() {
        Item bluntPickAxe = addItemToOldModel( "bluntpickaxe" );
        when( bluntPickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( bluntPickAxe ) ) );
        Item newItem = addItemToNewModel( "woodenpole" );
        ModelLocation itemLocation = addMockLocationToNewModel( "evensmallerannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_blunt_pick_axe_is_on_floor_of_annex_add_use_wooden_pole_and_axe_head_action() {
        Item bluntPickAxe = addItemToOldModel( "bluntpickaxe" );
        when( bluntPickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( bluntPickAxe ) ) );
        Item axeHead = addItemToNewModel( "axehead" );
        Item woodenPole = addItemToNewModel( "woodenpole" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( woodenPole,
                                                                 axeHead);
    }

    @Test
    public void if_blunt_pick_axe_is_not_visible_do_not_add_any_actions() {
        Item bluntPickAxe = addItemToOldModel( "bluntpickaxe" );
        when( bluntPickAxe.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "dimlylitannex" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( bluntPickAxe ) ) );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( Mockito.any( Item.class ) );
        verify( actionFactory, never() ).createTakeSpecificItemAction(
                                                            Mockito.any( Item.class ),
                                                            Mockito.any( UserInventory.class ),
                                                            Mockito.any( ModelLocation.class ) );
        verify( actionFactory, never() ).createUseWithSpecificItemAction(
                                                            Mockito.any( Item.class ),
                                                            Mockito.any( Item.class ) );
    }

    @Test
    public void if_blunt_pick_axe_has_been_picked_up_add_examine_chunk_of_metal_action() {
        addItemToOldModelInventory( "bluntpickaxe" );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( newAxeHead );
    }

    @Test
    public void if_blunt_pick_axe_has_been_picked_up_add_examine_straw_action() {
        addItemToOldModelInventory( "bluntpickaxe" );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }


    @Test
    public void if_blunt_pick_axe_has_been_picked_up_add_take_wooden_pole_action() {
        addItemToOldModelInventory( "bluntpickaxe" );
        Item newItem = addItemToNewModel( "woodenpole" );
        ModelLocation itemLocation = addMockLocationToNewModel( "evensmallerannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_blunt_pick_axe_has_been_picked_up_add_use_wooden_pole_and_axe_head_action() {
        addItemToOldModelInventory( "bluntpickaxe" );
        Item axeHead = addItemToNewModel( "axehead" );
        Item woodenPole = addItemToNewModel( "woodenpole" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( woodenPole,
                                                                 axeHead);
    }

    @Test
    public void if_blunt_pick_axe_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "bluntpickaxe" );
        Item newItem = addItemToNewModel( "bluntpickaxe" );
        ModelLocation itemLocation = addMockLocationToNewModel( "dimlylitannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

// Pick axe lifetime
    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_examine_chunk_of_metal_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( newAxeHead );
    }

    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_examine_straw_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }

    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_take_wooden_pole_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item newItem = addItemToNewModel( "woodenpole" );
        ModelLocation itemLocation = addMockLocationToNewModel( "evensmallerannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_use_wooden_pole_and_axe_head_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item axeHead = addItemToNewModel( "axehead" );
        Item woodenPole = addItemToNewModel( "woodenpole" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( woodenPole,
                                                                 axeHead );
    }

    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_take_blunt_axe_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item newItem = addItemToNewModel( "bluntpickaxe" );
        ModelLocation itemLocation = addMockLocationToNewModel( "dimlylitannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_pick_axe_is_on_floor_of_smithy_add_use_blunt_pick_axe_and_wheel_action() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe .visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item wheel = addItemToNewModel( "sharpeningwheel" );
        Item bluntPickAxe = addItemToNewModel( "bluntpickaxe" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( bluntPickAxe,
                                                                 wheel);
    }

    @Test
    public void if_pick_axe_is_not_visible_do_not_add_any_actions() {
        Item pickAxe = addItemToOldModel( "pickaxe" );
        when( pickAxe.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "minesmithy" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( pickAxe ) ) );
        Item wheel = addItemToNewModel( "sharpeningwheel" );
        Item bluntPickAxe = addItemToNewModel( "bluntpickaxe" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( Mockito.any( Item.class ) );
        verify( actionFactory, never() ).createTakeSpecificItemAction(
                                                            Mockito.any( Item.class ),
                                                            Mockito.any( UserInventory.class ),
                                                            Mockito.any( ModelLocation.class ) );
        verify( actionFactory, never() ).createUseWithSpecificItemAction(
                                                            Mockito.any( Item.class ),
                                                            Mockito.any( Item.class ) );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_examine_chunk_of_metal_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item newAxeHead = addItemToNewModel( "axehead" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( newAxeHead );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_examine_straw_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item pileOfStraw = addItemToNewModel( "pileofstraw" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( pileOfStraw );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_take_wooden_pole_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item newItem = addItemToNewModel( "woodenpole" );
        ModelLocation itemLocation = addMockLocationToNewModel( "evensmallerannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_use_wooden_pole_and_axe_head_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item axeHead = addItemToNewModel( "axehead" );
        Item woodenPole = addItemToNewModel( "woodenpole" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( woodenPole,
                                                                 axeHead );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_take_blunt_axe_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item newItem = addItemToNewModel( "bluntpickaxe" );
        ModelLocation itemLocation = addMockLocationToNewModel( "dimlylitannex" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_use_blunt_pick_axe_and_wheel_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item wheel = addItemToNewModel( "sharpeningwheel" );
        Item bluntPickAxe = addItemToNewModel( "bluntpickaxe" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( bluntPickAxe,
                                                                 wheel );
    }

    @Test
    public void if_pick_axe_has_been_picked_up_add_take_pick_axe_action() {
        addItemToOldModelInventory( "pickaxe" );
        Item newItem = addItemToNewModel( "pickaxe" );
        ModelLocation itemLocation = addMockLocationToNewModel( "minesmithy" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

// Map lifetime
    @Test
    public void if_map_is_on_floor_of_chamber_add_examine_table_action() {
        Item map = addItemToOldModel( "minemap" );
        when( map.visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "candlelitchamber" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( map ) ) );
        Item table = addItemToNewModel( "table" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( table );
    }

    @Test
    public void if_map_is_not_visible_do_not_add_examine_table_action() {
        Item map = addItemToOldModel( "minemap" );
        when( map.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "candlelitchamber" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( map ) ) );
        Item table = addItemToNewModel( "table" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createExamineAction( table );
    }

    @Test
    public void if_map_has_been_picked_up_add_examine_table_action() {
        addItemToOldModelInventory( "minemap" );
        Item table = addItemToNewModel( "table" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createExamineAction( table );
    }

    @Test
    public void if_map_has_been_picked_up_add_take_action() {
        addItemToOldModelInventory( "minemap" );
        Item newItem = addItemToNewModel( "minemap" );
        ModelLocation itemLocation = addMockLocationToNewModel( "candlelitchamber" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createTakeSpecificItemAction( newItem,
                                                              inventory,
                                                              itemLocation );
    }

// Shopkeeper lifetime
    @Test
    public void if_shop_keeper_is_in_cave_add_use_pick_axe_with_rock_action() {
        Item shopKeeper = addItemToOldModel( "shopkeeper" );
        when( shopKeeper.visible() ).thenReturn( true );
        ModelLocation location = addMockLocationToOldModel( "smallminechamber" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( shopKeeper ) ) );
        Item rock = addItemToNewModel( "rock" );
        Item pickAxe = addItemToNewModel( "pickaxe" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( pickAxe,
                                                                 rock );
    }

    @Test
    public void if_shop_keeper_is_not_visible_do_not_add_use_pick_axe_with_rock_action() {
        Item shopKeeper = addItemToOldModel( "shopkeeper" );
        when( shopKeeper.visible() ).thenReturn( false );
        ModelLocation location = addMockLocationToOldModel( "smallminechamber" );
        when( location.items() ).thenReturn( new ArrayList( Arrays.asList( shopKeeper ) ) );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory, never() ).createUseWithSpecificItemAction(
                                                                Mockito.any( Item.class ),
                                                                Mockito.any( Item.class ) );
    }

    @Test
    public void if_ruby_count_is_2_add_use_pick_axe_with_rock_action() {
        when( oldModel.currentScore() ).thenReturn( 2 );
        Item rock = addItemToNewModel( "rock" );
        Item pickAxe = addItemToNewModel( "pickaxe" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( pickAxe,
                                                                 rock );
    }

    @Test
    public void if_ruby_count_is_2_add_use_map_with_shop_keeper_action() {
        when( oldModel.currentScore() ).thenReturn( 2 );
        Item shopKeeper = addItemToNewModel( "shopkeeper" );
        Item map = addItemToNewModel( "minemap" );

        newConverter().inferActionsFrom( oldModel );

        verify( actionFactory ).createUseWithSpecificItemAction( map,
                                                                 shopKeeper );
    }
}

