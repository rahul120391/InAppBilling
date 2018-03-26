package assignments.android.zensar.myinappbilling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import assignments.android.zensar.myinappbilling.R;
import assignments.android.zensar.myinappbilling.interactors.IStarterView;
import assignments.android.zensar.myinappbilling.utility.Utils;

public class StarterActivity extends AppCompatActivity implements IStarterView, View.OnClickListener {

    AppCompatButton btnProduct;
    AppCompatButton btnSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        initViews();
    }

    @Override
    public void initViews() {
        btnProduct = findViewById(R.id.btnProduct);
        btnSubscription = findViewById(R.id.btnSubscription);
        btnProduct.setOnClickListener(this);
        btnSubscription.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnProduct) {
           Utils.startAcivity(this,InAppProductsActivity.class);
        } else if (v.getId() == R.id.btnSubscription) {
            Utils.startAcivity(this,SubscriptionActivity.class);
        }
    }
}
