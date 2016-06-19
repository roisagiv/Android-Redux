package io.roisagiv.android.redux.examples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import io.roisagiv.android.redux.examples.shoppingcart.ShoppingCartActivity;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private List<Example> examples;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    examples = Arrays.asList(new Example(ShoppingCartActivity.class));
    ListView examplesListView = (ListView) findViewById(R.id.listview_examples);
    examplesListView.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, examples));
    examplesListView.setOnItemClickListener(this);
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Example example = examples.get(position);
    startActivity(new Intent(this, example.activity));
  }

  private static class Example {
    private final Class<? extends Activity> activity;

    public Example(Class<? extends Activity> activity) {
      this.activity = activity;
    }

    @Override public String toString() {
      return activity.getSimpleName();
    }
  }
}
