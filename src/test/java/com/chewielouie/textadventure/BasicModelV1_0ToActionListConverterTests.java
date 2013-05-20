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

    // @Test
    // public void if_dust_of_the_ancients_has_been_picked_up_add_take_action() {
}

