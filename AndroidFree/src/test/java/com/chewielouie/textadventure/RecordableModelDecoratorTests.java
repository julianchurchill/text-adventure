package com.chewielouie.textadventure;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.junit.Test;
import com.chewielouie.textadventure.ActionHistory;

public class RecordableModelDecoratorTests {

    private Mockery mockery = new Mockery();

    @Test
    public void decorator_wraps_models_with_a_recordable_model() {
        RecordableModelDecorator decorator =
                new RecordableModelDecorator( null );

        TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventureModel decoratedModel = decorator.decorate( model );

        assertThat( decoratedModel, is( instanceOf( RecordableModel.class ) ) );
    }

    @Test
    public void decorator_passes_wrapped_model_to_new_model() {
        RecordableModelDecorator decorator =
                new RecordableModelDecorator( null );

        TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        RecordableModel decoratedModel = (RecordableModel)decorator.decorate( model );

        assertThat( decoratedModel.model(), is( model ) );
    }

    @Test
    public void decorator_passes_action_history_to_new_model() {
        ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        RecordableModelDecorator decorator =
                new RecordableModelDecorator( actionHistory );

        TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        RecordableModel decoratedModel = (RecordableModel)decorator.decorate( model );

        assertThat( decoratedModel.actionHistory(), is( actionHistory ) );
    }
}
