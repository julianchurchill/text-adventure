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
import com.chewielouie.textadventure.action.TakeSpecificItem;
import com.chewielouie.textadventure.item.Item;

public class BasicModelV1_0ToActionListConverterTests {
    @Test
    public void if_skeleton_key_has_been_picked_up_add_take_action() {
        Item skeletonKey = mock( Item.class );
        when( skeletonKey.id() ).thenReturn( "clocktowerskeletonkey" );
        List<Item> inventory = new ArrayList<Item>( Arrays.asList( skeletonKey ) );
        ModelLocation townEntrance = mock( ModelLocation.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        when( model.inventoryItems() ).thenReturn( inventory );
        when( model.findLocationByID( "townentrance" ) ).thenReturn( townEntrance );
        BasicModelV1_0ToActionListConverter converter =
            new BasicModelV1_0ToActionListConverter( model );

        Action action = converter.actions().get( 0 );
        assertThat( action, is( instanceOf( TakeSpecificItem.class ) ) );
        assertThat( ((TakeSpecificItem)action).item(), is( skeletonKey ) );
        assertThat( ((TakeSpecificItem)action).location(), is( townEntrance ) );
    }

    // @Test
    // public void if_skeleton_key_has_been_picked_up_add_take_action_using_factory() {

    // @Test
    // public void if_banana_peel_has_been_picked_up_add_take_action() {
}

