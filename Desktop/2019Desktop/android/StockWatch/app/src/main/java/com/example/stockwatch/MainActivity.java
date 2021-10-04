package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Stock> stockList = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private StocksAdapter sAdapter;
    private RecyclerView recyclerView;
    private StockDatabaseHandler sdbHandler;
    private HashMap<String, String> hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler);
        swiper = findViewById(R.id.swiper);

        sAdapter = new StocksAdapter(stockList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sAdapter);

        sdbHandler = new StockDatabaseHandler(this);
        new StockInternetDataLoader(MainActivity.this).execute();
        ArrayList<Stock> tempList = sdbHandler.loadStocks();
        if (connectionCheck()) {
            for (int i = 0; i < tempList.size(); i++) {
                new StockFinancialDataLoader(MainActivity.this).
                        execute(tempList.get(i).getSymbol());
            }
        } else {
            stockList.addAll(tempList);
            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connectionCheck()) {
                    doRefresh();
                } else {
                    swiper.setRefreshing(false);
                    neterrorDialog();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        sAdapter.notifyDataSetChanged();
    }

    private void doRefresh() {
        ArrayList<Stock> tempList1 = sdbHandler.loadStocks();
        for (int i = 0; i < tempList1.size(); i++) {
            String symbol = tempList1.get(i).getSymbol();
            new StockFinancialDataLoader(MainActivity.this).execute(symbol);
        }
        swiper.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (!connectionCheck()) {
            neterrorDialog();
            return false;
        } else {
            switch (id) {
                case R.id.add_stock:
                    addStockDialog();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int i = recyclerView.getChildLayoutPosition(view);
        String URL = "http://www.marketwatch.com/investing/stock/" + stockList.get(i).getSymbol();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URL));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        final int id = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icons8);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol"+((TextView)view.findViewById(R.id.symbolView)).getText().toString()+" ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sdbHandler.deleteStock(stockList.get(id).getSymbol());
                stockList.remove(id);
                sAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    private boolean connectionCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnected();
    }

    private void neterrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot Be Added Without A Network Connection");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean dupStockCheck(String s) {
        String sym = s.split(" - ")[0];
        Stock tempStock = new Stock();
        tempStock.setSymbol(sym);
        return stockList.contains(tempStock);
    }

    private void dupStockDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon3);
        builder.setTitle("Duplicate Stock");
        builder.setMessage("Stock Symbol " + s + " is already displayed");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void SymbolNotFoundDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Symbol Not Found: "+ s);
        builder.setMessage("Data for stock symbol "+ s +" not found");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<String> searchStock(String s) {
        ArrayList<String> optStock = new ArrayList<>();
        if(hashMap != null && !hashMap.isEmpty()) {
            for (String symbol : hashMap.keySet()) {
                String company = hashMap.get(symbol);
                if (symbol.toUpperCase().contains(s.toUpperCase())) {
                    optStock.add(symbol + " - " + company);
                } else if (company.toUpperCase().contains(s.toUpperCase())) {
                    optStock.add(symbol + " -" + company);
                }
            }
        }
        return optStock;
    }

    private void saveStock(String s) {
        String symbol = s.split(" - ")[0];
        new StockFinancialDataLoader(MainActivity.this).execute(symbol);
        Stock tStock = new Stock();
        tStock.setSymbol(symbol);
        tStock.setCompany(hashMap.get(symbol));
        sdbHandler.addStock(tStock);

    }
    public void setStockData(HashMap<String, String> hashMap) {
        if (hashMap != null && !hashMap.isEmpty()) {
            this.hashMap = hashMap;
        }
    }

    public void setStockInfo(Stock stock) {
        if (stock != null) {
            int index = stockList.indexOf(stock);
            if (index > -1) {
                stockList.remove(index);
            }
            stockList.add(stock);
            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }
    }

    private void stockSelectDialog(final String s, ArrayList<String> optlist, int i) {
        final String[] str = new String[i];
        for(int j = 0; j< str.length; j++){
            str[j] = optlist.get(j);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a Selection");
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dupStockCheck(str[which])){
                    dupStockDialog(s);
                }
                else {
                    saveStock(str[which]);
                }
            }
        });
        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addStockDialog() {
        if (hashMap == null) {
            new StockInternetDataLoader(MainActivity.this).execute();
        }
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd.setTitle("Stock Selection");
        bd.setMessage("Please Enter Stock Symbol:");
        bd.setIcon(R.drawable.icon2);
        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        bd.setView(et);
        bd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = et.getText().toString();
                if (!connectionCheck()) {
                    neterrorDialog();
                    return;
                } else if (s.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Valid Value. Like 'AAP' ",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ArrayList<String> tList = searchStock(s);
                    if (!tList.isEmpty()) {
                        ArrayList<String> stock_list = new ArrayList<>(tList);
                        if (stock_list.size() == 1) {
                            if (dupStockCheck(stock_list.get(0))) {
                                dupStockDialog(s);
                            } else {
                                saveStock(stock_list.get(0));
                            }
                        } else {
                            stockSelectDialog(s, stock_list, stock_list.size());
                        } } else {
                        SymbolNotFoundDialog(s);
                    }
                }
            }
        });
        bd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = bd.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdbHandler.shutDown();
    }
}
