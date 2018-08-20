package com.brox.livewallpaper.alien_blood;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidLauncher extends Activity implements RewardedVideoAdListener {

	Intent intent;
	private InterstitialAd mInterstitialAd;
	private RewardedVideoAd mRewardedVideoAd;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
					new ComponentName(getPackageName(), getPackageName() + ".LiveWallpaper"));
			startActivityForResult(intent, 666);
		} catch (android.content.ActivityNotFoundException e3) {
            System.out.println("All failed 3! "+e3.getMessage());
			try {
				intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				startActivityForResult(intent, 667);
			} catch (android.content.ActivityNotFoundException e2) {
				try {
					intent = new Intent();
					intent.setAction("com.bn.nook.CHANGE_WALLPAPER");
					startActivityForResult(intent, 668);
					System.out.println("All failed 2! "+e2.getMessage());
				} catch (android.content.ActivityNotFoundException e) {
					System.out.println("All failed! "+e.getMessage());
				}
			}
		}

		// Admob
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-4693918988103938~5963080185");

		// Video Ad
		mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
		mRewardedVideoAd.setRewardedVideoAdListener(this);
		mRewardedVideoAd.loadAd("ca-app-pub-4693918988103938/2239457590",
				new AdRequest.Builder().addTestDevice("B5555DF3ED3732B4BA0DDA03F167E983").build());

		// Interestial Ad
		mInterstitialAd = new InterstitialAd(getApplicationContext());
		mInterstitialAd.setAdUnitId("ca-app-pub-4693918988103938/7123268553");
		mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B5555DF3ED3732B4BA0DDA03F167E983").build());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mInterstitialAd.isLoaded()) {
			//mInterstitialAd.show();
		}
		finish();
	}

	@Override
	protected void onPause() {
		mRewardedVideoAd.pause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		mRewardedVideoAd.resume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mRewardedVideoAd.destroy(this);
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		//mRewardedVideoAd.show();
	}

	@Override
	public void onRewardedVideoAdOpened() {
	}

	@Override
	public void onRewardedVideoStarted() {
	}

	@Override
	public void onRewardedVideoAdClosed() {
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {
	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
	}

	@Override
	public void onRewardedVideoCompleted() {
	}
}
