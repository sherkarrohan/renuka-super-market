
package com.renuka.supermarket;
import android.content.*; import android.database.sqlite.*; import android.database.*;
public class DB extends SQLiteOpenHelper {
 public DB(Context c){super(c,"renuka_supermarket.db",null,2);}
 public void onCreate(SQLiteDatabase d){
  d.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,barcode TEXT,category TEXT,buy REAL DEFAULT 0,sell REAL DEFAULT 0,qty REAL DEFAULT 0,minqty REAL DEFAULT 0,unit TEXT DEFAULT 'pcs',supplier TEXT,hsn TEXT,gst REAL DEFAULT 0)");
  d.execSQL("CREATE TABLE sales(id INTEGER PRIMARY KEY AUTOINCREMENT,dt INTEGER,total REAL,subtotal REAL,gst REAL,discount REAL,profit REAL,payment TEXT)");
  d.execSQL("CREATE TABLE sale_items(id INTEGER PRIMARY KEY AUTOINCREMENT,sale_id INTEGER,product_id INTEGER,name TEXT,qty REAL,price REAL,cost REAL,gst REAL)");
  d.execSQL("CREATE TABLE purchases(id INTEGER PRIMARY KEY AUTOINCREMENT,dt INTEGER,total REAL,supplier TEXT,invoice TEXT)");
  d.execSQL("CREATE TABLE purchase_items(id INTEGER PRIMARY KEY AUTOINCREMENT,purchase_id INTEGER,product_id INTEGER,qty REAL,cost REAL)");
  d.execSQL("CREATE TABLE expenses(id INTEGER PRIMARY KEY AUTOINCREMENT,dt INTEGER,title TEXT,amount REAL)");
  d.execSQL("CREATE TABLE stock_log(id INTEGER PRIMARY KEY AUTOINCREMENT,dt INTEGER,product_id INTEGER,delta REAL,reason TEXT)");
 }
 public void onUpgrade(SQLiteDatabase d,int o,int n){d.execSQL("DROP TABLE IF EXISTS stock_log");d.execSQL("DROP TABLE IF EXISTS sale_items");d.execSQL("DROP TABLE IF EXISTS sales");d.execSQL("DROP TABLE IF EXISTS purchase_items");d.execSQL("DROP TABLE IF EXISTS purchases");d.execSQL("DROP TABLE IF EXISTS expenses");d.execSQL("DROP TABLE IF EXISTS products");onCreate(d);}
 public long addProduct(String n,String b,String cat,double buy,double sell,double q,double min,String unit,String sup,String hsn,double gst){
  ContentValues v=new ContentValues();v.put("name",n);v.put("barcode",b);v.put("category",cat);v.put("buy",buy);v.put("sell",sell);v.put("qty",q);v.put("minqty",min);v.put("unit",unit);v.put("supplier",sup);v.put("hsn",hsn);v.put("gst",gst);return getWritableDatabase().insert("products",null,v);
 }
 public Cursor products(String s){return getReadableDatabase().rawQuery("SELECT * FROM products WHERE name LIKE ? OR barcode LIKE ? ORDER BY name",new String[]{"%"+s+"%","%"+s+"%"});}
 public Cursor product(long id){return getReadableDatabase().rawQuery("SELECT * FROM products WHERE id=?",new String[]{""+id});}
 public void updatePrice(long id,double buy,double sell){getWritableDatabase().execSQL("UPDATE products SET buy=?,sell=? WHERE id=?",new Object[]{buy,sell,id});}
 public void stock(long id,double delta,String reason){getWritableDatabase().execSQL("UPDATE products SET qty=qty+? WHERE id=?",new Object[]{delta,id});ContentValues v=new ContentValues();v.put("dt",System.currentTimeMillis());v.put("product_id",id);v.put("delta",delta);v.put("reason",reason);getWritableDatabase().insert("stock_log",null,v);}
 public long sale(double total,double subtotal,double gst,double discount,double profit,String pay){ContentValues v=new ContentValues();v.put("dt",System.currentTimeMillis());v.put("total",total);v.put("subtotal",subtotal);v.put("gst",gst);v.put("discount",discount);v.put("profit",profit);v.put("payment",pay);return getWritableDatabase().insert("sales",null,v);}
 public void saleItem(long sid,long pid,String n,double q,double p,double c,double gst){ContentValues v=new ContentValues();v.put("sale_id",sid);v.put("product_id",pid);v.put("name",n);v.put("qty",q);v.put("price",p);v.put("cost",c);v.put("gst",gst);getWritableDatabase().insert("sale_items",null,v);}
 public long purchase(double total,String supplier,String invoice){ContentValues v=new ContentValues();v.put("dt",System.currentTimeMillis());v.put("total",total);v.put("supplier",supplier);v.put("invoice",invoice);return getWritableDatabase().insert("purchases",null,v);}
 public void purchaseItem(long pid,long product,double q,double cost){ContentValues v=new ContentValues();v.put("purchase_id",pid);v.put("product_id",product);v.put("qty",q);v.put("cost",cost);getWritableDatabase().insert("purchase_items",null,v);}
 public long expense(String title,double amount){ContentValues v=new ContentValues();v.put("dt",System.currentTimeMillis());v.put("title",title);v.put("amount",amount);return getWritableDatabase().insert("expenses",null,v);}
 public Cursor report(long f,long t){return getReadableDatabase().rawQuery("SELECT COALESCE(SUM(total),0),COALESCE(SUM(profit),0),COUNT(*),COALESCE(SUM(gst),0),COALESCE(SUM(discount),0) FROM sales WHERE dt BETWEEN ? AND ?",new String[]{""+f,""+t});}
 public Cursor expenseSum(long f,long t){return getReadableDatabase().rawQuery("SELECT COALESCE(SUM(amount),0) FROM expenses WHERE dt BETWEEN ? AND ?",new String[]{""+f,""+t});}
 public Cursor low(){return getReadableDatabase().rawQuery("SELECT name,qty,minqty,unit FROM products WHERE qty<=minqty ORDER BY qty",null);}
 public Cursor sales(){return getReadableDatabase().rawQuery("SELECT id,dt,total,profit,payment FROM sales ORDER BY dt DESC",null);}
}
