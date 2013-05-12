package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;

public class BasicActionHistoryTests {

    @Test
    public void size_starts_at_zero() {
        BasicActionHistory history = new BasicActionHistory();
        assertThat( history.size(), is( 0 ) );
    }

    @Test
    public void size_is_equal_to_the_number_of_actions_added() {
        BasicActionHistory history = new BasicActionHistory();
        history.addActionWithParameters( null, null );
        history.addActionWithParameters( null, null );
        history.addActionWithParameters( null, null );
        assertThat( history.size(), is( 3 ) );
    }

    @Test
    public void actions_added_to_history_can_be_retrieved_in_the_order_they_were_added() {
        Action action1 = mock( Action.class );
        Action action2 = mock( Action.class );
        Action action3 = mock( Action.class );
        ActionParameters params1 = mock( ActionParameters.class );
        ActionParameters params2 = mock( ActionParameters.class );
        ActionParameters params3 = mock( ActionParameters.class );

        BasicActionHistory history = new BasicActionHistory();
        history.addActionWithParameters( action1, params1 );
        history.addActionWithParameters( action2, params2 );
        history.addActionWithParameters( action3, params3 );

        assertThat( history.getRecord( 0 ).action(), is( action1 ) );
        assertThat( history.getRecord( 0 ).params(), is( params1 ) );
        assertThat( history.getRecord( 1 ).action(), is( action2 ) );
        assertThat( history.getRecord( 1 ).params(), is( params2 ) );
        assertThat( history.getRecord( 2 ).action(), is( action3 ) );
        assertThat( history.getRecord( 2 ).params(), is( params3 ) );
    }

    @Test
    public void clear_empties_the_history() {
        BasicActionHistory history = new BasicActionHistory();
        history.addActionWithParameters( null, null );

        history.clear();

        assertThat( history.size(), is( 0 ) );
    }
}
