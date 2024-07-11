package com.privacy.bizzle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;
import java.io.*;

public class MainActivity extends Activity {
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private EditText edittext1;
	private ImageView search;
	private ProgressBar progressbar1;
	private WebView webview1;
	private ImageView imageview1;
	private ImageView back;
	private ImageView forward;
	private ImageView reload;
	private ImageView exit;
	
	private SharedPreferences sf;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		linear4 = findViewById(R.id.linear4);
		edittext1 = findViewById(R.id.edittext1);
		search = findViewById(R.id.search);
		progressbar1 = findViewById(R.id.progressbar1);
		webview1 = findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		imageview1 = findViewById(R.id.imageview1);
		back = findViewById(R.id.back);
		forward = findViewById(R.id.forward);
		reload = findViewById(R.id.reload);
		exit = findViewById(R.id.exit);
		sf = getSharedPreferences("last", Activity.MODE_PRIVATE);
		
		edittext1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				((EditText)edittext1).selectAll();
				return true;
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext1.getText().toString().contains(" ")) {
					webview1.loadUrl("https://start.duckduckgo.com/".concat(edittext1.getText().toString()));
				}
				else {
					if (edittext1.getText().toString().contains(".")) {
						webview1.loadUrl(edittext1.getText().toString());
					}
					else {
						webview1.loadUrl("https://start.duckduckgo.com/".concat(edittext1.getText().toString()));
					}
				}
				SketchwareUtil.hideKeyboard(getApplicationContext());
			}
		});
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				progressbar1.setVisibility(View.VISIBLE);
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				edittext1.setText(_url);
				progressbar1.setVisibility(View.GONE);
				sf.edit().putString("last", _url).commit();
				super.onPageFinished(_param1, _param2);
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.loadUrl("https://start.duckduckgo.com");
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.goBack();
			}
		});
		
		forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.goForward();
			}
		});
		
		reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.loadUrl(webview1.getUrl());
			}
		});
		
		exit.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Clear & Exit");
				return true;
			}
		});
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.clearCache(true);
				webview1.clearHistory();
				clearData();
				finishAffinity();
			}
		});
	}
	
	private void initializeLogic() {
		edittext1.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				  //kindly see upper block for action ids
				     if(actionId ==3){ 
					search.performClick();
					SketchwareUtil.hideKeyboard(getApplicationContext());
					
					  }   
				          return false;
				      }
		});
		edittext1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFFFFFFF));
		webview1.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity (intent);
			}
		});
		if (sf.getString("last", "").equals("")) {
			webview1.loadUrl("https://start.duckduckgo.com");
		}
		else {
			webview1.loadUrl(sf.getString("last", ""));
		}
	}
	
	@Override
	public void onBackPressed() {
		webview1.goBack();
	}
	
	public void _extra() {
	}
	private void clearData() {
		            try {
			                if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
				                    ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
				                } else {
				                    Runtime.getRuntime().exec("pm clear " + getApplicationContext().getPackageName());
				                }
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
		    }
	{
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}