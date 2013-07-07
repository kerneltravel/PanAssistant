package com.crazy.panassistant.business.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSErrorCode;
import com.baidu.pcs.BaiduPCSStatusListener;
import com.crazy.panassistant.actvity.ListActivity;
import com.crazy.panassistant.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class BaiDuOpenAPI {

	// token
	private static String mbOauth = null;

	// the handler
	private Handler mbUiThreadHandler = new Handler();

	/**
	 * 得到网盘的空间大小(total:总大小，used:已用大小）
	 * 
	 * @return
	 */
	public Map<String, Long> getQuota() {
		final Map<String, Long> map = new HashMap<String, Long>();
		if (null != mbOauth) {
			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					final BaiduPCSActionInfo.PCSQuotaResponse info = api
							.quota();
					map.put("total", info.total);
					map.put("used", info.used);
				}
			});
			workThread.start();
		}
		return map;
	}

	/**
	 * 登陆百度网盘
	 * 
	 * @param activity
	 *            登陆界面的activity对象
	 */
	public void login(final Activity activity) {
		BaiduOAuth oauthClient = new BaiduOAuth();
		oauthClient.startOAuth(activity, Constants.API_KEY, new String[] {
				"basic", "netdisk" }, new BaiduOAuth.OAuthListener() {
			@Override
			public void onException(String msg) {
				Toast.makeText(activity.getApplicationContext(), "登陆失败 " + msg,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(BaiduOAuthResponse response) {
				if (null != response) {
					mbOauth = response.getAccessToken();
					System.out.println("mbOauth======" + mbOauth);
					Toast.makeText(activity.getApplicationContext(), "登陆成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(activity, ListActivity.class);
					intent.putExtra("accessToken", mbOauth);
					activity.startActivity(intent);
				}
			}

			@Override
			public void onCancel() {
				Toast.makeText(activity.getApplicationContext(), "取消登陆",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void upload(final String file, final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					int len = file.split("/").length;
					String fileName = file.split("/")[len - 1];
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);

					final BaiduPCSActionInfo.PCSFileInfoResponse response = api
							.uploadFile(file, Constants.mbRootPath + fileName,
									new BaiduPCSStatusListener() {

										@Override
										public void onProgress(long bytes,
												long total) {
											// TODO Auto-generated method stub

											final long bs = bytes;
											final long tl = total;

											mbUiThreadHandler
													.post(new Runnable() {
														public void run() {
															Toast.makeText(
																	activity.getApplicationContext(),
																	"total: "
																			+ tl
																			+ "    sent:"
																			+ bs,
																	Toast.LENGTH_SHORT)
																	.show();
														}
													});
										}

										@Override
										public long progressInterval() {
											return 1000;
										}
									});

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									response.status.errorCode + "  "
											+ response.status.message + "  "
											+ response.commonFileInfo.blockList,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void delete(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);

					List<String> files = new ArrayList<String>();
					// files.add(mbRootPath + "/" + "198.jpg");
					// files.add(mbRootPath + "/" + "2.jpg");
					// files.add(mbRootPath + "/" + "3.jpg");
					//
					final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api
							.deleteFiles(files);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Delete files:  " + ret.errorCode + "  "
											+ ret.message, Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void download(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String source = Constants.mbRootPath + "/189.jpg";
					String target = "/mnt/sdcard/DCIM/100MEDIA/yytest0801.mp4";
					final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api
							.downloadFileFromStream(source, target,
									new BaiduPCSStatusListener() {
										// yangyangdd
										@Override
										public void onProgress(long bytes,
												long total) {
											// TODO Auto-generated method stub
											final long bs = bytes;
											final long tl = total;

											mbUiThreadHandler
													.post(new Runnable() {
														public void run() {
															Toast.makeText(
																	activity.getApplicationContext(),
																	"total: "
																			+ tl
																			+ "    downloaded:"
																			+ bs,
																	Toast.LENGTH_SHORT)
																	.show();
														}
													});
										}

										@Override
										public long progressInterval() {
											return 500;
										}

									});

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Download files:  " + ret.errorCode + "   "
											+ ret.message, Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void mkdir(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String path = Constants.mbRootPath + "/" + "JakeDu";

					final BaiduPCSActionInfo.PCSFileInfoResponse ret = api
							.makeDir(path);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Mkdir:  " + ret.status.errorCode + "   "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void meta(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String path = Constants.mbRootPath + "/08_HTTP.mp4";

					final BaiduPCSActionInfo.PCSMetaResponse ret = api
							.meta(path);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {

							String extra = null;

							switch (ret.type) {
							case Media_Audio:
								BaiduPCSActionInfo.PCSAudioMetaResponse audioInfo = (BaiduPCSActionInfo.PCSAudioMetaResponse) ret;
								if (null != audioInfo) {
									extra = audioInfo.trackTitle;
								}
								break;

							case Media_Video:
								BaiduPCSActionInfo.PCSVideoMetaResponse videoInfo = (BaiduPCSActionInfo.PCSVideoMetaResponse) ret;
								if (null != videoInfo) {
									extra = videoInfo.resolution;
								}
								break;

							case Media_Image:
								BaiduPCSActionInfo.PCSImageMetaResponse imageInfo = (BaiduPCSActionInfo.PCSImageMetaResponse) ret;
								if (null != imageInfo) {
									extra = imageInfo.latitude + "  "
											+ imageInfo.longtitude;
								}
								break;
							}

							Toast.makeText(
									activity.getApplicationContext(),
									"Meta:  " + ret.status.errorCode + "   "
											+ ret.status.message + "  " + extra,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void list(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String path = Constants.mbRootPath;

					final BaiduPCSActionInfo.PCSListInfoResponse ret = api
							.list(path, "name", "asc");
					// final BaiduPCSActionInfo.PCSListInfoResponse ret =
					// api.imageStream();
					System.out.println(" ret.status.message---------->>>"
							+ ret.status.message);
					System.out.println("ret.status.errorCode---------->>>"
							+ ret.status.errorCode);
					System.out.println("list---------->>>" + ret.list);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"List:  " + ret.status.errorCode + "    "
											+ ret.status.message + ret.list,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});

			workThread.start();
		}
	}

	public void _move(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					// String from = mbRootPath + "/1.txt";
					// String to = mbRootPath + "/Jake/2.txt"; //
					// test1122335665.jpg

					List<BaiduPCSActionInfo.PCSFileFromToInfo> info = new ArrayList<BaiduPCSActionInfo.PCSFileFromToInfo>();
					BaiduPCSActionInfo.PCSFileFromToInfo data1 = new BaiduPCSActionInfo.PCSFileFromToInfo();
					data1.from = Constants.mbRootPath + "/JakeDu/08_HTTP.jpg";
					data1.to = Constants.mbRootPath + "/08_HTTP.mp4";

					info.add(data1);
					// info.add(data2);

					final BaiduPCSActionInfo.PCSFileFromToResponse ret = api
							.move(info);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Move:  " + ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});

			workThread.start();
		}
	}

	public void copy(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					// String from = mbRootPath + "/1.txt";
					// String to = mbRootPath + "/Jake/2.txt"; //
					// test1122335665.jpg

					List<BaiduPCSActionInfo.PCSFileFromToInfo> info = new ArrayList<BaiduPCSActionInfo.PCSFileFromToInfo>();
					BaiduPCSActionInfo.PCSFileFromToInfo data1 = new BaiduPCSActionInfo.PCSFileFromToInfo();
					data1.from = Constants.mbRootPath + "/08_HTTP.mp4";
					data1.to = Constants.mbRootPath + "/JakeDu/08_HTTP.jpg";

					BaiduPCSActionInfo.PCSFileFromToInfo data2 = new BaiduPCSActionInfo.PCSFileFromToInfo();
					data2.from = Constants.mbRootPath + "/1986.jpg";
					data2.to = Constants.mbRootPath + "/JakeDu/6.jpg";

					info.add(data1);
					// info.add(data2);

					final BaiduPCSActionInfo.PCSFileFromToResponse ret = api
							.copy(info);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Copy:  " + ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});

			workThread.start();
		}
	}

	public void _search(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);

					final BaiduPCSActionInfo.PCSListInfoResponse ret = api
							.search(Constants.mbRootPath, "jpg", true);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Search:  " + ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void diff(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);

					final BaiduPCSActionInfo.PCSDiffResponse ret = api
							.diff("lPoXQ82tTNeQi17NfzbqlefWLhWlMZzDqioifhVxuA1ZMIOK3Da4gAEep+KIyVue3Iuy+tEQn9CpBPg8C4p8Imt7ypPiaLhF8ShPiNctAUBrtXcKhX/O80LUnmlhtwWosB3bJtl9i99y5QFE6zNAwEae5PL1JxAkxi3vQoNr2XYLnGv2r/u08o3SW0axqqj6qRo3f9rFxX36CkQhWZUGG7XOelgBPlus0d7CGObNs9ltH9OustCKLiTQXG2G96Ap");
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Diff:  " + ret.status.errorCode + "   "
											+ ret.status.message + "  "
											+ ret.entries.size(),
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});

			workThread.start();
		}
	}

	public void cloudmatch(final Activity activity) {
		if (null != mbOauth) {
			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					final BaiduPCSActionInfo.PCSFileInfoResponse ret = api
							.cloudMatch("/mnt/sdcard/r", Constants.mbRootPath
									+ "/fjar");
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"CloudMatch:  " + ret.status.errorCode
											+ "\n" + ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});
			workThread.start();
		}
	}

	//
	// move
	//
	private void rename(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					// String from = mbRootPath + "/10mpic.jpg";
					// String to = "big10mpic.jpg"; // test1122335665.jpg
					// String from = null;
					// String to = mbRootPath + "/10mpic.jpg"; //
					// test1122335665.jpg
					// final BaiduPCSActionInfo.PCSFileFromToResponse ret =
					// api.rename(from, to);
					List<BaiduPCSActionInfo.PCSFileFromToInfo> info = new ArrayList<BaiduPCSActionInfo.PCSFileFromToInfo>();
					BaiduPCSActionInfo.PCSFileFromToInfo data1 = new BaiduPCSActionInfo.PCSFileFromToInfo();
					BaiduPCSActionInfo.PCSFileFromToInfo data2 = new BaiduPCSActionInfo.PCSFileFromToInfo();
					data1.from = Constants.mbRootPath + "/fhjk/r";
					data1.to = "r";
					// data1.to = null;

					// data2.from = mbRootPath + "/fhj/1111.jpg";
					// data2.to = "11newname.jpg";

					info.add(data1);
					// info.add(data2);

					final BaiduPCSActionInfo.PCSFileFromToResponse ret = api
							.rename(info);

					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"Rename:  " + ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});

			workThread.start();
		}
	}

	//
	// cloud match and upload if cloud match false
	//
	public void test_cloudmatchupload(final Activity activity) {
		if (null != mbOauth) {
			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					final BaiduPCSActionInfo.PCSFileInfoResponse ret = api
							.cloudMatchAndUploadFile("/mnt/sdcard/yy.rar",
									Constants.mbRootPath
											+ "/yangyangdd/yy1.rar",
									new BaiduPCSStatusListener() {

										@Override
										public void onProgress(long bytes,
												long total) {
											// TODO Auto-generated method stub
											final long bs = bytes;
											final long tl = total;

											mbUiThreadHandler
													.post(new Runnable() {
														public void run() {
															Toast.makeText(
																	activity.getApplicationContext(),
																	"total: "
																			+ tl
																			+ "    upload:"
																			+ bs,
																	Toast.LENGTH_SHORT)
																	.show();
														}
													});
										}

										@Override
										public long progressInterval() {
											return 1000;
										}

									});
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"CloudMatchandUpload:  "
											+ ret.status.errorCode + "\n"
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});

				}
			});
			workThread.start();
		}
	}

	//
	// thumbnail
	//
	public void test_thumbnail(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String picpath = Constants.mbRootPath + "/" + "198.jpg";
					final BaiduPCSActionInfo.PCSThumbnailResponse ret = api
							.thumbnail(picpath, 100, 90, 90);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {

							if (BaiduPCSErrorCode.No_Error == ret.status.errorCode) {
								Toast.makeText(
										activity.getApplicationContext(),
										"Thumbnail   success"
												+ ret.status.errorCode + "    "
												+ ret.status.message,
										Toast.LENGTH_SHORT).show();

								if (null != ret && null != ret.bitmap) {
									// iv.setImageBitmap(ret.bitmap);
								}
							} else {
								Toast.makeText(
										activity.getApplicationContext(),
										"Thumbnail   failed: "
												+ ret.status.errorCode + "   "
												+ ret.status.message,
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			});

			workThread.start();
		}

	}

	//
	// cloud download
	//
	private void test_clouddownload(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String destPath = Constants.mbRootPath + "/Skycn_1.2.1.exe";
					String sourceUrl = "http://tk.wangyuehd.com/soft/Skycn_1.2.1.exe";// http://59.108.246.24:82/down/kis12.0.0.374zh-Hans_cn.zip";
					// String sourceUrl =
					// "http://59.108.246.24:82/down/QQsetup.zip";
					final BaiduPCSActionInfo.PCSCloudDownloadResponse ret = api
							.cloudDownload(sourceUrl, destPath);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"cloudDownload:  " + ret.status.errorCode
											+ "    " + ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			workThread.start();
		}
	}

	//
	// cloud download list
	//
	private void test_clouddownloadlist(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					final BaiduPCSActionInfo.PCSCloudDownloadTaskListResponse ret = api
							.cloudDownloadTaskList();
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"cloudDownloadlist:  "
											+ ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			workThread.start();
		}
	}

	//
	// query task status
	//
	private void test_queryclouddownloadtask(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String[] queryTaskId = { "2404" };
					final BaiduPCSActionInfo.PCSCloudDownloadQueryTaskProgressResponse ret = api
							.queryCloudDownloadTaskProgress(queryTaskId);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"querycloudDownloadtask:  "
											+ ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			workThread.start();
		}
	}

	//
	// cancel a task
	//
	private void test_cancelclouddownloadtask(final Activity activity) {
		if (null != mbOauth) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {
					BaiduPCSClient api = new BaiduPCSClient();
					api.setAccessToken(mbOauth);
					String queryTaskId = "2312";
					final BaiduPCSActionInfo.PCSCloudDownloadResponse ret = api
							.cancelCloudDownloadTask(queryTaskId);
					mbUiThreadHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(
									activity.getApplicationContext(),
									"cancelcloudDownloadtask:  "
											+ ret.status.errorCode + "    "
											+ ret.status.message,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			workThread.start();
		}
	}

	//
	// logout
	//
	public void test_logout(final Activity activity) {
		if (null != mbOauth) {
			/**
			 * you can call this method to logout in Android 4.0.3
			 */
			// BaiduOAuth oauth = new BaiduOAuth();
			// oauth.logout(mbOauth, new BaiduOAuth.ILogoutListener(){
			//
			// @Override
			// public void onResult(boolean success) {
			//
			// Toast.makeText(getApplicationContext(), "Logout: " + success,
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// });

			/**
			 * you can call this method to logout in Android 2.X
			 */
			Thread workThread = new Thread(new Runnable() {
				@Override
				public void run() {

					BaiduOAuth oauth = new BaiduOAuth();
					final boolean ret = oauth.logout(mbOauth);
					mbUiThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity.getApplicationContext(),
									"Logout " + ret, Toast.LENGTH_SHORT).show();
						}
					});

				}
			});

			workThread.start();
		}

	}
}
