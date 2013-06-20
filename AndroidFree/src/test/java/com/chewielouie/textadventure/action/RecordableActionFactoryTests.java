package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSource;
import java.util.List;
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
    public void wraps_Action_in_RecordableAction_for_ShowInventory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createShowInventoryAction( null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_ShowInventory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createShowInventoryAction( null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
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
    public void wraps_Action_in_RecordableAction_for_InventoryItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createInventoryItemAction( null, null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_InventoryItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createInventoryItemAction( null, null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_InventoryItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createInventoryItemAction( item, null, null );
        assertThat( a.actionParameters().item(), is( item ) );
    }

    @Test
    public void adds_Location_to_RecordableAction_for_InventoryItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( location );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createInventoryItemAction( null,
                                                            null, location );
        assertThat( a.actionParameters().location(), is( location ) );
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
    public void wraps_Action_in_RecordableAction_for_Examine() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createExamineAction( null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_Examine() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createExamineAction( null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_Examine() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createExamineAction( item );
        assertThat( a.actionParameters().item(), is( item ) );
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
    public void wraps_Action_in_RecordableAction_for_UseWith() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createUseWithAction( null, null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_UseWith() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createUseWithAction( null, null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_UseWith() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createUseWithAction( item, null, null );
        assertThat( a.actionParameters().item(), is( item ) );
    }

    @Test
    public void adds_Location_to_RecordableAction_for_UseWith() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( location );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createUseWithAction( null, null, location );
        assertThat( a.actionParameters().location(), is( location ) );
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
    public void wraps_Action_in_RecordableAction_for_ExamineAnItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createExamineAnItemAction( null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_ExamineAnItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createExamineAnItemAction( null );
        assertThat( a.actionHistory(), is( actionHistory ) );
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
    public void wraps_Action_in_RecordableAction_for_TakeAnItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createTakeAnItemAction( null, null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_TakeAnItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createTakeAnItemAction( null, null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Location_to_RecordableAction_for_TakeAnItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( location );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createTakeAnItemAction( null, null, location );
        assertThat( a.actionParameters().location(), is( location ) );
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
    public void wraps_Action_in_RecordableAction_for_TakeSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createTakeSpecificItemAction( null, null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_TakeSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createTakeSpecificItemAction( null, null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_TakeSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createTakeSpecificItemAction( item, null, null );
        assertThat( a.actionParameters().item(), is( item ) );
    }

    @Test
    public void adds_Location_to_RecordableAction_for_TakeSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( location );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createTakeSpecificItemAction( null,
                                                            null, location );
        assertThat( a.actionParameters().location(), is( location ) );
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
    public void wraps_Action_in_RecordableAction_for_UseWithSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createUseWithSpecificItemAction( null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_UseWithSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createUseWithSpecificItemAction( null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_UseWithSpecificItem() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item actionOwner = mockery.mock( Item.class, "actionOwner" );
        final Item target = mockery.mock( Item.class, "target" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionOwner );
            ignoring( target );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createUseWithSpecificItemAction(
            actionOwner, target );
        assertThat( a.actionParameters().item(), is( actionOwner ) );
        assertThat( a.actionParameters().extraItem(), is( target ) );
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

    @Test
    public void wraps_Action_in_RecordableAction_for_Exit() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createExitAction( null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_Exit() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createExitAction( null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Exit_to_RecordableAction_for_Exit() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( exit );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createExitAction( exit, null );
        assertThat( a.actionParameters().exit(), is( exit ) );
    }

    // Don't know how to write this in JMock - Expectations need to be set before
    // constructing RecordableActionFactory but the factory is needed for the Expectations!
    // @Test
    // public void sets_self_as_factory_for_child_actions_on_wrapped_factory() {
    //     final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
    //     final RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
    //     mockery.checking( new Expectations() {{
    //         oneOf( wrappedFactory ).setFactoryForChildActionsToUse( f );
    //         ignoring( wrappedFactory );
    //     }});
    // }

    @Test
    public void sets_factory_for_child_actions_on_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        final ActionFactory otherFactory = mockery.mock( ActionFactory.class, "other" );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).setFactoryForChildActionsToUse( otherFactory );
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        f.setFactoryForChildActionsToUse( otherFactory );
    }

    @Test
    public void create_talk_to_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createTalkToAction( item );
            ignoring( wrappedFactory );
        }});
        new RecordableActionFactory( wrappedFactory, null ) .createTalkToAction( item );
    }

    @Test
    public void wraps_Action_in_RecordableAction_for_TalkTo() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createTalkToAction( null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_TalkTo() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( actionHistory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createTalkToAction( null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_Item_to_RecordableAction_for_TalkTo() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createTalkToAction( item );
        assertThat( a.actionParameters().item(), is( item ) );
    }

    @Test
    public void create_say_action_delegates_to_wrapped_factory() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( wrappedFactory ).createSayAction( "id", item );
            ignoring( wrappedFactory );
            ignoring( item );
        }});
        new RecordableActionFactory( wrappedFactory, null )
            .createSayAction( "id", item );
    }

    @Test
    public void wraps_Action_in_RecordableAction_for_Say() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class, "wrapped" );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory, null );
        assertThat( f.createSayAction( null, null ),
                    is( instanceOf( RecordableAction.class ) ) );
    }

    @Test
    public void adds_ActionHistory_to_RecordableAction_for_Say() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 actionHistory );
        RecordableAction a = (RecordableAction)f.createSayAction( null, null );
        assertThat( a.actionHistory(), is( actionHistory ) );
    }

    @Test
    public void adds_id_to_RecordableAction_for_Say() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        mockery.checking( new Expectations() {{
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createSayAction( "id", null );
        assertThat( a.actionParameters().string(), is( "id" ) );
    }

    @Test
    public void adds_talk_phrase_source_to_RecordableAction_for_Say() {
        final ActionFactory wrappedFactory = mockery.mock( ActionFactory.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( wrappedFactory );
        }});
        RecordableActionFactory f = new RecordableActionFactory( wrappedFactory,
                                                                 null );
        RecordableAction a = (RecordableAction)f.createSayAction( null, item );
        assertThat( a.actionParameters().item(), is( item ) );
    }
}
