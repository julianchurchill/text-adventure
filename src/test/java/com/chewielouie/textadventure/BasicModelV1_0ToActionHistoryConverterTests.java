package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.action.TakeSpecificItem;
import com.chewielouie.textadventure.item.Item;

public class BasicModelV1_0ToActionHistoryConverterTests {
    @Test
    public void if_skeleton_key_has_been_picked_up_add_take_action() {
        Item skeletonKey = mock( Item.class );
        when( skeletonKey.id() ).thenReturn( "clocktowerskeletonkey" );
        List<Item> inventory = new ArrayList<Item>( Arrays.asList( skeletonKey ) );
        ModelLocation townEntrance = mock( ModelLocation.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        when( model.inventoryItems() ).thenReturn( inventory );
        when( model.findLocationByID( "townentrance" ) ).thenReturn( townEntrance );
        BasicModelV1_0ToActionHistoryConverter converter =
            new BasicModelV1_0ToActionHistoryConverter( model );

        Action action = converter.actionHistory().getRecord( 0 ).action();
        assertThat( action, is( instanceOf( TakeSpecificItem.class ) ) );
        assertThat( ((TakeSpecificItem)action).item(), is( skeletonKey ) );
        assertThat( ((TakeSpecificItem)action).location(), is( townEntrance ) );
// Do we really need to check this twice - seems like overkill?
        ActionParameters params = converter.actionHistory().getRecord( 0 ).params();
        assertThat( params.item(), is( skeletonKey ) );
        assertThat( params.location(), is( townEntrance ) );
    }

    // @Test
    // public void if_banana_peel_has_been_picked_up_add_take_action() {
}

