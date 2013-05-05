package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class RecordableActionFactoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void create_show_inventory_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createShowInventoryAction( inventory, model );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createShowInventoryAction( inventory, model );
    }

    @Test
    public void create_inventory_item_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createInventoryItemAction( item, inventory, location );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createInventoryItemAction( item, inventory, location );
    }

    @Test
    public void create_examine_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createExamineAction( item );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createExamineAction( item );
    }

    @Test
    public void create_use_with_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createUseWithAction( item, inventory, location );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createUseWithAction( item, inventory, location );
    }

    @Test
    public void create_examine_an_item_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final List<Item> items = mockery.mock( List.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createExamineAnItemAction( items );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createExamineAnItemAction( items );
    }

    @Test
    public void create_take_an_item_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final List<Item> items = mockery.mock( List.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createTakeAnItemAction( items, inventory, location );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createTakeAnItemAction( items, inventory, location );
    }

    @Test
    public void create_take_specific_item_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createTakeSpecificItemAction( item, inventory, location );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createTakeSpecificItemAction( item, inventory, location );
    }

    @Test
    public void create_use_with_specific_item_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item actionOwner = mockery.mock( Item.class, "action owner" );
        final Item target = mockery.mock( Item.class, "target" );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createUseWithSpecificItemAction( actionOwner, target );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createUseWithSpecificItemAction( actionOwner, target );
    }

    @Test
    public void create_exit_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Exit exit = mockery.mock( Exit.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createExitAction( exit, model );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null )
                .createExitAction( exit, model );
    }
}
