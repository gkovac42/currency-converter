package com.example.goran.currencyconverter.converter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goran.currencyconverter.BaseApplication;
import com.example.goran.currencyconverter.R;
import com.example.goran.currencyconverter.data.model.Currency;
import com.example.goran.currencyconverter.di.ConverterActivityModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;

public class ConverterActivity extends AppCompatActivity implements ConverterContract.View {

    private static final int HRK_POSITION = 0;
    private static final String BUNDLE_RESULT = "bundle_result";

    @BindView(R.id.txt_input) EditText txtInput;
    @BindView(R.id.txt_result) TextView txtResult;
    @BindView(R.id.spinner_from) Spinner spinnerFrom;
    @BindView(R.id.spinner_to) Spinner spinnerTo;

    @Inject
    ConverterContract.Presenter presenter;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        ButterKnife.bind(this);

        (((BaseApplication) getApplication()).getAppComponent())
                .converterActivitySubcomponent(new ConverterActivityModule(this))
                .inject(this);

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
        }

        presenter.getData(isNetworkAvailable());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!txtResult.getText().toString().isEmpty()) {
            outState.putString(BUNDLE_RESULT, txtResult.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void loadSpinnersData(List<Currency> currencies) {
        ArrayAdapter<Currency> spinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);

        spinnerFrom.setAdapter(spinnerAdapter);
        spinnerTo.setAdapter(spinnerAdapter);

        if (savedInstanceState != null) {
            txtResult.setText(savedInstanceState.getString(BUNDLE_RESULT));
        }
    }

    /* Here I followed the logic of the API converter (http://hnbex.eu/) which converts
       HRK to other currencies or other currencies to HRK, and adapts the other spinner's selected item accordingly */
    @OnItemSelected(R.id.spinner_from)
    public void spinnerFromItemSelected(int position) {
        if (position != HRK_POSITION) {
            spinnerTo.setSelection(HRK_POSITION);
        }
    }

    @OnItemSelected(R.id.spinner_to)
    public void spinnerToItemSelected(int position) {
        if (position != HRK_POSITION) {
            spinnerFrom.setSelection(HRK_POSITION);
        }
    }

    @OnEditorAction(R.id.txt_input)
    public boolean onEditorActionDone(int action) {
        if (action == EditorInfo.IME_ACTION_DONE) {
            onClickSubmit();
        }
        return false;
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        try {
            processUserInput();

        } catch (NumberFormatException e) {
            presenter.onInputError();
        }
    }

    private void processUserInput() throws NumberFormatException {
        double quantity = Double.parseDouble(txtInput.getText().toString());
        Currency fromCurrency = (Currency) spinnerFrom.getSelectedItem();
        Currency toCurrency = (Currency) spinnerTo.getSelectedItem();

        presenter.onClickSubmit(quantity, fromCurrency, toCurrency);
    }

    @OnClick(R.id.btn_clear)
    void onClickClear() {
        presenter.onClickClear();
    }

    @Override
    public void clearInput() {
        txtInput.setText("");
        txtResult.setText("");
    }

    @Override
    public void displayResult(String result) {
        txtResult.setText(result);
    }

    @Override
    public void displayNetworkError() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayInputError() {
        Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayDatabaseError() {
        Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
    }
}
