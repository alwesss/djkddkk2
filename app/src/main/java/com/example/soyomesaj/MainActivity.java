package com.example.soyomesaj;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String PREFS = "soyo_helper_prefs";
    private static final String PREF_TEMPLATE = "template";
    private static final String PREF_TITLE = "title";
    private static final String PREF_AUTO_OPEN = "auto_open_recommendation";
    private static final String PREF_AUTO_MODE = "full_auto_mode";
    private static final String DEFAULT_TEMPLATE = "{isim} {hitap} selamlarrrr";
    private static final String SOYO_PACKAGE = "com.haflla.soulu";
    private static final String SOYO_LITE_PACKAGE = "com.haflla.soulu.lite";

    private Spinner titleSpinner;
    private EditText templateInput;
    private TextView preview;
    private TextView serviceStatus;
    private Button serviceButton;
    private CheckBox autoOpenCheck;
    private CheckBox autoModeCheck;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        int pad = dp(18);
        int gap = dp(10);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);
        scroll.addView(root);

        TextView heading = new TextView(this);
        heading.setText("SOYO Tek Onay");
        heading.setTextSize(26);
        heading.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(heading);

        TextView info = new TextView(this);
        info.setText("İstersen tek onayla kullan; istersen Tam Otomatik Mod ile kişi bulma, sohbeti açma, mesajı gönderme, geri dönme ve kaydırmayı otomatikleştir.");
        info.setTextSize(16);
        root.addView(info, top(gap));

        serviceStatus = new TextView(this);
        serviceStatus.setTextSize(17);
        serviceStatus.setTypeface(Typeface.DEFAULT_BOLD);
        serviceStatus.setPadding(dp(12), dp(14), dp(12), dp(14));
        serviceStatus.setBackgroundResource(android.R.drawable.editbox_background);
        root.addView(serviceStatus, top(gap * 2));

        serviceButton = new Button(this);
        serviceButton.setText("ERİŞİLEBİLİRLİK İZNİNİ AÇ");
        serviceButton.setMinHeight(dp(56));
        serviceButton.setOnClickListener(v -> openAccessibilitySettings());
        root.addView(serviceButton, top(gap));

        TextView how = new TextView(this);
        how.setText("İlk kurulumda bir kez: Ayarlar → Erişilebilirlik → SOYO Tek Onay → Aç. Sonrasında yardımcı uygulamayı açık tutman gerekmez.");
        how.setTextSize(14);
        root.addView(how, top(dp(6)));

        autoOpenCheck = new CheckBox(this);
        autoOpenCheck.setText("Önerilenler ekranında ilk uygun Sohbet satırını otomatik aç");
        autoOpenCheck.setTextSize(15);
        autoOpenCheck.setChecked(prefs.getBoolean(PREF_AUTO_OPEN, true));
        autoOpenCheck.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(PREF_AUTO_OPEN, isChecked).apply());
        root.addView(autoOpenCheck, top(gap * 2));

        autoModeCheck = new CheckBox(this);
        autoModeCheck.setText("Tam Otomatik Mod (kaydır, sohbeti aç ve mesajı gönder)");
        autoModeCheck.setTextSize(16);
        autoModeCheck.setTypeface(Typeface.DEFAULT_BOLD);
        autoModeCheck.setChecked(prefs.getBoolean(PREF_AUTO_MODE, false));
        autoModeCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(PREF_AUTO_MODE, isChecked).apply();
            if (isChecked && autoOpenCheck != null && !autoOpenCheck.isChecked()) {
                autoOpenCheck.setChecked(true);
            }
            Toast.makeText(this,
                    isChecked ? "Tam otomatik mod açıldı." : "Tam otomatik mod kapatıldı.",
                    Toast.LENGTH_SHORT).show();
        });
        root.addView(autoModeCheck, top(dp(6)));

        TextView autoWarning = new TextView(this);
        autoWarning.setText("Bu seçenek açıkken mesaj ONAYLA beklemeden gönderilir. Kapatınca mevcut tek-onay akışı devam eder.");
        autoWarning.setTextSize(13);
        root.addView(autoWarning, top(dp(3)));

        TextView screenRule = new TextView(this);
        screenRule.setText("Ekran görüntündeki düzene göre isim, sarı “Sohbet” düğmesiyle aynı satırdaki soldaki yazıdan alınır. VIP, km, yaş ve alt menü metinleri isim olarak kullanılmaz.");
        screenRule.setTextSize(13);
        root.addView(screenRule, top(dp(4)));

        TextView titleLabel = new TextView(this);
        titleLabel.setText("Hitap");
        titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(titleLabel, top(gap * 2));

        titleSpinner = new Spinner(this);
        String[] titles = new String[]{"Bey", "Hanım", "Hitap yok"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, titles);
        titleSpinner.setAdapter(adapter);
        int savedTitle = prefs.getInt(PREF_TITLE, 0);
        if (savedTitle >= 0 && savedTitle < titles.length) titleSpinner.setSelection(savedTitle);
        root.addView(titleSpinner, top(dp(4)));

        TextView templateLabel = new TextView(this);
        templateLabel.setText("Mesaj şablonu");
        templateLabel.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(templateLabel, top(gap * 2));

        templateInput = new EditText(this);
        templateInput.setText(prefs.getString(PREF_TEMPLATE, DEFAULT_TEMPLATE));
        templateInput.setMinLines(2);
        templateInput.setGravity(Gravity.TOP);
        templateInput.setHint(DEFAULT_TEMPLATE);
        root.addView(templateInput, top(dp(4)));

        preview = new TextView(this);
        preview.setTextSize(20);
        preview.setTypeface(Typeface.DEFAULT_BOLD);
        preview.setPadding(dp(14), dp(16), dp(14), dp(16));
        preview.setBackgroundResource(android.R.drawable.editbox_background);
        root.addView(preview, top(gap));

        TextView hint = new TextView(this);
        hint.setText("Örnek: Berk algılanırsa → “Berk Bey selamlarrrr”. {isim} ve {hitap} otomatik değiştirilir.");
        hint.setTextSize(13);
        root.addView(hint, top(dp(5)));

        Button openSoyo = new Button(this);
        openSoyo.setText("SOYO'YU AÇ");
        openSoyo.setMinHeight(dp(56));
        openSoyo.setOnClickListener(v -> openSoyo());
        root.addView(openSoyo, top(gap * 2));

        TextView privacy = new TextView(this);
        privacy.setText("Servis yalnızca SOYO paketlerinde çalışır. Tam Otomatik Mod kapalıyken gönderme düğmesine sadece ONAYLA'ya dokunduğunda basar. Tam Otomatik Mod açıkken gönderir, listeye döner ve sıradaki kişi için devam eder.");
        privacy.setTextSize(12);
        root.addView(privacy, top(gap));

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { refreshPreview(); }
            @Override public void afterTextChanged(Editable s) {}
        };
        templateInput.addTextChangedListener(watcher);
        titleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                prefs.edit().putInt(PREF_TITLE, position).apply();
                refreshPreview();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        setContentView(scroll);
        refreshPreview();
        refreshServiceStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshServiceStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    private void saveSettings() {
        if (templateInput == null || titleSpinner == null) return;
        String template = templateInput.getText().toString().trim();
        if (template.isEmpty()) template = DEFAULT_TEMPLATE;
        prefs.edit()
                .putString(PREF_TEMPLATE, template)
                .putInt(PREF_TITLE, titleSpinner.getSelectedItemPosition())
                .putBoolean(PREF_AUTO_OPEN, autoOpenCheck == null || autoOpenCheck.isChecked())
                .putBoolean(PREF_AUTO_MODE, autoModeCheck != null && autoModeCheck.isChecked())
                .apply();
    }

    private void refreshPreview() {
        if (preview == null || templateInput == null || titleSpinner == null) return;
        preview.setText(buildMessage("Berk"));
    }

    private String buildMessage(String name) {
        String selected = String.valueOf(titleSpinner.getSelectedItem());
        String title = "Hitap yok".equals(selected) ? "" : selected;
        String template = templateInput.getText().toString().trim();
        if (template.isEmpty()) template = DEFAULT_TEMPLATE;
        return template
                .replace("{isim}", name)
                .replace("{hitap}", title)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void openAccessibilitySettings() {
        saveSettings();
        try {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "Listeden “SOYO Tek Onay”ı aç.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erişilebilirlik ayarları açılamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshServiceStatus() {
        boolean enabled = isAccessibilityServiceEnabled();
        if (serviceStatus != null) {
            serviceStatus.setText(enabled ? "✓ Otomatik algılama AÇIK" : "○ Otomatik algılama KAPALI");
        }
        if (serviceButton != null) {
            serviceButton.setText(enabled ? "ERİŞİLEBİLİRLİK AYARLARINI AÇ" : "ERİŞİLEBİLİRLİK İZNİNİ AÇ");
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        ComponentName expected = new ComponentName(this, SoyoAccessibilityService.class);
        String enabledServices = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (TextUtils.isEmpty(enabledServices)) return false;
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(enabledServices);
        while (splitter.hasNext()) {
            ComponentName enabled = ComponentName.unflattenFromString(splitter.next());
            if (expected.equals(enabled)) return true;
        }
        return false;
    }

    private void openSoyo() {
        saveSettings();
        Intent launch = getPackageManager().getLaunchIntentForPackage(SOYO_PACKAGE);
        if (launch == null) launch = getPackageManager().getLaunchIntentForPackage(SOYO_LITE_PACKAGE);
        if (launch != null) {
            startActivity(launch);
            return;
        }
        Toast.makeText(this, "SOYO bulunamadı; mağaza açılıyor.", Toast.LENGTH_SHORT).show();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + SOYO_PACKAGE)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + SOYO_PACKAGE)));
        }
    }

    private LinearLayout.LayoutParams top(int margin) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.topMargin = margin;
        return p;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
