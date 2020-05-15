package com.tristar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;
import com.tristar.main.R;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.SessionData;

import org.kobjects.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class CustomAdapter extends BaseAdapter implements Swappable, OnClickListener, UndoAdapter, OnDismissCallback {

	private static LayoutInflater inflater = null;
	public Resources res;
	ProcessAddressForServer detail = new ProcessAddressForServer();
	ProcessAddressForServer processOrderForTable;
	String[] separated;
	String[] Time_separated;
	private Activity activity;
	private ArrayList<ProcessAddressForServer> List;
	private boolean mShouldShowDragAndDropIcon;

	public CustomAdapter(Activity a, ArrayList<ProcessAddressForServer> list, boolean shouldShowDragAndDropIcon) {
		activity = a;
		List = list;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
	}

	public ArrayList<ProcessAddressForServer> getList()
	{
		return this.List;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}


	@SuppressLint("LongLogTag")
	public int getCount() {
		Log.d("The Checklist Array Size", "" + List.size());
		return List.size();
	}

	public Object getItem(int position) {
		return List.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {

		return 1;
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}

	@Override
	public void swapItems(int positionOne, int positionTwo) {
		Collections.swap(List, positionOne, positionTwo);
	}

	@SuppressLint({"InflateParams", "SetTextI18n"})
	public View getView(final int position, View convertView, ViewGroup parent) {

		//View vi = convertView;
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.process_order_child, null);
			//convertView	vi = inflater.inflate(R.layout.list_courtservice_items,parent, false);

			holder = new ViewHolder();
			holder.list_table = (LinearLayout) convertView.findViewById(R.id.list_table);
			holder.txt_type_parent = (LinearLayout) convertView.findViewById(R.id.txt_type_parent);
			holder.ListView_Processorder = (LinearLayout) convertView.findViewById(R.id.listView_Processorder);
			holder.attach = (ImageView) convertView.findViewById(R.id.attach);
			holder.list_navig = (ImageView) convertView.findViewById(R.id.list_navig);
			holder.Lblitem1cs = (TextView) convertView.findViewById(R.id.txt_udno);
			holder.Lblitem2cs = (TextView) convertView.findViewById(R.id.txt_addresslist);
			holder.textWide = (TextView) convertView.findViewById(R.id.txtsereee);
			holder.check = (CheckBox) convertView.findViewById(R.id.check_process_order);
			holder.txt_due_date = (TextView) convertView.findViewById(R.id.txt_due_date);
			holder.txt_priority_title = (TextView) convertView.findViewById(R.id.txt_priority_title);
			holder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);
			holder.txt_status = (TextView) convertView.findViewById(R.id.txt_status);


			/*holder.list_table.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					processOrderForTable = List.get(position);

					activity.finish();
					Intent processdetail = new Intent(activity,ProcessOrderDetail.class);
					processdetail.putExtra("processOrderID",
							processOrderForTable.getProcessOrderID());
                    activity.startActivity(processdetail);
				}
			});*/

//			holder.attach_white.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//					Toast.makeText(activity,"button Click",Toast.LENGTH_SHORT).show();
//                }
//            });


			if (SessionData.getInstance().getCheckvisible() == 1) {
				holder.check.setVisibility(View.VISIBLE);
			} else if (SessionData.getInstance().getCheckvisible() == 2) {
				holder.check.setVisibility(View.GONE);
			}
			convertView.setTag(holder);


		} else
			holder = (ViewHolder) convertView.getTag();

		if (getCount() <= 0) {
			holder.Lblitem1cs.setText("No Data");
			holder.Lblitem2cs.setText("No Data");
			holder.textWide.setText("No Data");

		} else {
			ProcessAddressForServer object = (ProcessAddressForServer) List.get(position);
			holder.id = position;
			object.setId(holder.id);

			String title = object.getMilestoneTitle();

			if (title.length() == 0){
				holder.txt_status.setText("");
				//holder.txt_status.setVisibility(View.GONE);
			}else {
				holder.txt_status.setText(title);
			}



			String Check_Today_Priority = object.getPriorityTitle();
			boolean Today_priority = Pattern.compile(Pattern.quote("Today"), Pattern.CASE_INSENSITIVE)
					.matcher(object.getPriorityTitle()).find();


			if (Today_priority) {
				holder.ListView_Processorder.setBackground(activity.getDrawable(R.drawable.white_background));
				holder.list_navig.setImageResource(R.drawable.navigation_next_item_black);
//				holder.list_navig.setBackground(activity.getDrawable(R.drawable.navigation_next_item_black));


				if (object.getPriorityTitle() == null || object.getPriorityTitle().length() == 0) {
					holder.txt_priority_title.setText("");
				} else {
					holder.txt_priority_title.setText(object.getPriorityTitle());
				}


				if (object.getWorkorder() == null || object.getWorkorder().length() == 0) {
					holder.Lblitem1cs.setText("");
				} else {
					holder.Lblitem1cs.setText(object.getWorkorder());
				}

				String Status_title = object.getMilestoneTitle();

				if (Status_title.length() == 0){
					holder.txt_status.setText("");
					//holder.txt_status.setVisibility(View.GONE);
				}else {
					holder.txt_status.setText(Status_title);

				}

//				String Address = object.getAddressFormattedForDisplay();
//				separated = Address.split(":");
//
//				if (separated[0] == null || separated[0].length() == 0) {
//					holder.txt_type.setText("");
//					holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
//				} else {
//					boolean addresstype = Pattern.compile(Pattern.quote("Home"), Pattern.CASE_INSENSITIVE)
//							.matcher(separated[0]).find();
//					if (addresstype) {
//						holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
//						holder.txt_type.setText("H");
//						holder.txt_type.setTextColor(Color.WHITE);
//					} else {
//						boolean addresstype1 = Pattern.compile(Pattern.quote("Business"), Pattern.CASE_INSENSITIVE)
//								.matcher(separated[0]).find();
//						if (addresstype1) {
//							holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
//							holder.txt_type.setText("B");
//							holder.txt_type.setTextColor(Color.WHITE);
//						} else {
//
//							boolean addresstype2 = Pattern.compile(Pattern.quote("Government"), Pattern.CASE_INSENSITIVE)
//									.matcher(separated[0]).find();
//							if (addresstype2) {
//								holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
//								holder.txt_type.setText("G");
//								holder.txt_type.setTextColor(Color.WHITE);
//							} else {
//								boolean addresstype3 = Pattern.compile(Pattern.quote("Not a physical Address"), Pattern.CASE_INSENSITIVE)
//										.matcher(separated[0]).find();
//								if (addresstype3) {
//									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
//									holder.txt_type.setText("NPA");
//									holder.txt_type.setTextColor(Color.WHITE);
//									holder.txt_type.setTextSize(10);
//								} else {
//									boolean addresstype4 = Pattern.compile(Pattern.quote("Other"), Pattern.CASE_INSENSITIVE)
//											.matcher(separated[0]).find();
//									if (addresstype4) {
//										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
//										holder.txt_type.setText("O");
//										holder.txt_type.setTextColor(Color.WHITE);
//									} else {
//										holder.txt_type.setText("");
//										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
//									}
//								}
//							}
//						}
//					}
//				}
//
//				if (separated[1].trim().length() != 0) {
//					holder.Lblitem2cs.setText(separated[1].trim());
//					holder.Lblitem2cs.setTextColor(Color.BLACK);
//				} else {
//					holder.Lblitem2cs.setText("");
//				}


				String Address = object.getAddressFormattedForDisplay();

				if(object.getAddressFormattedNewLine1().length()!=0){
					holder.Lblitem2cs.setText(object.getAddressFormattedNewLine1()+"\n"+object.getAddressFormattedNewLine2());

				}
				//holder.Lblitem2cs.setText(object.getAddressFormattedNewLine1()+"\n"+object.getAddressFormattedNewLine2());
				if (Address != null || Address.length() != 0) {
					separated = Address.split(":");
					if (separated.length == 0) {
						if(object.getAddressFormattedNewLine1().length()==0) {
								holder.Lblitem2cs.setText(Address);
						}
						holder.txt_type.setText("");
						holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));

					} else {
						if (separated.length == 1) {
							if(object.getAddressFormattedNewLine1().length()==0) {
									holder.Lblitem2cs.setText(separated[0].trim());
							}
							holder.txt_type.setText("");
							holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
						} else {


							if (separated[0].length() == 0) {
								holder.txt_type.setText("");
								holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
								//holder.Lblitem2cs.setText("N/A");
							} else {
								if (separated[0].contains("Home")) {
									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
									holder.txt_type.setText("H");
									holder.txt_type.setTextColor(Color.WHITE);
								} else if ((separated[0].contains("Business"))) {
									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
									holder.txt_type.setText("B");
									holder.txt_type.setTextColor(Color.WHITE);

								} else if ((separated[0].contains("Government"))) {
									holder.txt_type.setText("G");
									holder.txt_type.setTextColor(Color.WHITE);
									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
								} else {
									if ((separated[0].contains("Not a physical Address"))) {
										holder.txt_type.setText("NPA");
										holder.txt_type.setTextColor(Color.WHITE);
										holder.txt_type.setTextSize(10);
										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
									} else if ((separated[0].contains("Other"))) {
										holder.txt_type.setText("O");
										holder.txt_type.setTextColor(Color.WHITE);
										holder.txt_type.setTextSize(10);
										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
									}
								}
							}

							//if(separated.length>1){
							if (separated[1].length() == 0) {
								if(object.getAddressFormattedNewLine1().length()==0) {
										holder.Lblitem2cs.setText("N/A");
								}
							} else {
								if (separated[1].trim().length() != 0) {
									if(object.getAddressFormattedNewLine1().length()==0) {
										holder.Lblitem2cs.setText(separated[1].trim());
									}
//									holder.Lblitem2cs.setTextColor(Color.WHITE);
								} else {
									if(object.getAddressFormattedNewLine1().length()==0) {
									holder.Lblitem2cs.setText("");
									}
								}
							}
						}
					}
				}

				if (object.getServee() == null || object.getServee().length() == 0) {
					holder.textWide.setText("");
//					holder.textWide.setTextColor(Color.BLACK);

				} else {
					holder.textWide.setText(object.getServee());
//					holder.textWide.setTextColor(Color.BLACK);
				}

				String Due_Date = object.getDueDate();
				Time_separated = Due_Date.split("T");
				if (Time_separated.length != 0) {
					String date = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);
					holder.txt_due_date.setText(date.trim());
//					holder.txt_due_date.setTextColor(Color.BLACK);

				} else {
					holder.txt_due_date.setText("");

				}

				if (object.isHasAttachments()) {
					holder.attach.setImageResource(R.drawable.attach_black);
					holder.attach.setVisibility(View.VISIBLE);
				} else {
					holder.attach.setVisibility(View.GONE);
				}

				holder.txt_priority_title.setTextColor(activity.getResources().getColor(R.color.light_orange));
				holder.Lblitem1cs.setTextColor(activity.getResources().getColor(R.color.light_orange));
				holder.txt_status.setTextColor(activity.getResources().getColor(R.color.light_orange));
				holder.Lblitem2cs.setTextColor(activity.getResources().getColor(R.color.light_orange));
				holder.textWide.setTextColor(activity.getResources().getColor(R.color.light_orange));
				holder.txt_due_date.setTextColor(activity.getResources().getColor(R.color.light_orange));


			} else {
				holder.ListView_Processorder.setBackground(activity.getDrawable(R.drawable.black_background));
				holder.list_navig.setImageResource(R.drawable.ic_navigation_next_item);
//				holder.list_navig.setBackground(activity.getDrawable(R.drawable.ic_navigation_next_item));
				if (object.getPriorityTitle() == null || object.getPriorityTitle().length() == 0) {
					holder.txt_priority_title.setText("");
				} else {
					boolean Rush_priority = Pattern.compile(Pattern.quote("Rush"), Pattern.CASE_INSENSITIVE)
							.matcher(object.getPriorityTitle()).find();

					if (Rush_priority) {

						if (object.getPriorityTitle() == null || object.getPriorityTitle().length() == 0) {
							holder.txt_priority_title.setText("");
						} else {
							holder.txt_priority_title.setText(object.getPriorityTitle());
						}

						if (object.getWorkorder() == null || object.getWorkorder().length() == 0) {
							holder.Lblitem1cs.setText("");
						} else {
							holder.Lblitem1cs.setText(object.getWorkorder());
						}

						String Status_title = object.getMilestoneTitle();

						if (Status_title.length() == 0) {
							holder.txt_status.setText("");
						} else {
							holder.txt_status.setText(Status_title);

						}

						String Address = object.getAddressFormattedForDisplay();
						if(object.getAddressFormattedNewLine1().length()!=0) {
							holder.Lblitem2cs.setText(object.getAddressFormattedNewLine1()+"\n"+object.getAddressFormattedNewLine2());

						}
						if (Address != null || Address.length() != 0) {
							separated = Address.split(":");
							if (separated.length == 0) {
								if(object.getAddressFormattedNewLine1().length()==0) {
										holder.Lblitem2cs.setText(Address);
								}
								holder.txt_type.setText("");
								holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));

							} else {
								if (separated.length == 1) {
									if(object.getAddressFormattedNewLine1().length()==0) {
											holder.Lblitem2cs.setText(separated[0].trim());
									}
									holder.txt_type.setText("");
									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
								} else {


									if (separated[0].length() == 0) {
										holder.txt_type.setText("");
										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
										if(object.getAddressFormattedNewLine1().length()==0) {
												holder.Lblitem2cs.setText("N/A");
										}
									} else {
										if (separated[0].contains("Home")) {
											holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
											holder.txt_type.setText("H");
											holder.txt_type.setTextColor(Color.WHITE);
										} else if ((separated[0].contains("Business"))) {
											holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
											holder.txt_type.setText("B");
											holder.txt_type.setTextColor(Color.WHITE);

										} else if ((separated[0].contains("Government"))) {
											holder.txt_type.setText("G");
											holder.txt_type.setTextColor(Color.WHITE);
											holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
										} else {
											if ((separated[0].contains("Not a physical Address"))) {
												holder.txt_type.setText("NPA");
												holder.txt_type.setTextColor(Color.WHITE);
												holder.txt_type.setTextSize(10);
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
											} else if ((separated[0].contains("Other"))) {
												holder.txt_type.setText("O");
												holder.txt_type.setTextColor(Color.WHITE);
												holder.txt_type.setTextSize(10);
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
											}
										}
									}

									//if(separated.length>1){
									if (separated[1].length() == 0) {
										//holder.Lblitem2cs.setText("N/A");
									} else {
										if (separated[1].trim().length() != 0) {
											if(object.getAddressFormattedNewLine1().length()==0) {
													holder.Lblitem2cs.setText(separated[1].trim());
											}
//									holder.Lblitem2cs.setTextColor(Color.WHITE);
										} else {
											if(object.getAddressFormattedNewLine1().length()==0) {
													holder.Lblitem2cs.setText("");
											}
										}
									}
								}
							}
						}

						if (object.getServee() == null || object.getServee().length() == 0) {
							holder.textWide.setText("");
//					holder.textWide.setTextColor(Color.BLACK);

						} else {
							holder.textWide.setText(object.getServee());
//					holder.textWide.setTextColor(Color.BLACK);
						}

						String Due_Date = object.getDueDate();
						Time_separated = Due_Date.split("T");
						if (Time_separated.length != 0) {
							String date = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);
							holder.txt_due_date.setText(date.trim());
//					holder.txt_due_date.setTextColor(Color.BLACK);

						} else {
							holder.txt_due_date.setText("");

						}

						if (object.isHasAttachments()) {
							holder.attach.setVisibility(View.VISIBLE);
						} else {
							holder.attach.setVisibility(View.GONE);
						}

						holder.txt_priority_title.setTextColor(activity.getResources().getColor(R.color.red));
						holder.Lblitem1cs.setTextColor(activity.getResources().getColor(R.color.red));
						holder.txt_status.setTextColor(activity.getResources().getColor(R.color.red));
						holder.Lblitem2cs.setTextColor(activity.getResources().getColor(R.color.red));
						holder.textWide.setTextColor(activity.getResources().getColor(R.color.red));
						holder.txt_due_date.setTextColor(activity.getResources().getColor(R.color.red));

					} else if (!Rush_priority) {
						boolean Standard_priority = Pattern.compile(Pattern.quote("Standard"), Pattern.CASE_INSENSITIVE)
								.matcher(object.getPriorityTitle()).find();
						if (Standard_priority) {


							if (object.getPriorityTitle() == null || object.getPriorityTitle().length() == 0) {
								holder.txt_priority_title.setText("");
							} else {
								holder.txt_priority_title.setText(object.getPriorityTitle());
							}

							if (object.getWorkorder() == null || object.getWorkorder().length() == 0) {
								holder.Lblitem1cs.setText("");
							} else {
								holder.Lblitem1cs.setText(object.getWorkorder());
							}

							String Status_title = object.getMilestoneTitle();

							if (Status_title.length() == 0) {
								holder.txt_status.setText("");
							} else {
								holder.txt_status.setText(Status_title);

							}

							String Address = object.getAddressFormattedForDisplay();
							if(object.getAddressFormattedNewLine1().length()!=0) {
								holder.Lblitem2cs.setText(object.getAddressFormattedNewLine1() + "\n" + object.getAddressFormattedNewLine2());
							}
							if (Address != null || Address.length() != 0) {
								separated = Address.split(":");
								if (separated.length == 0) {
									if(object.getAddressFormattedNewLine1().length()==0) {
											holder.Lblitem2cs.setText(Address);
									}
									holder.txt_type.setText("");
									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));

								} else {
									if (separated.length == 1) {
										if(object.getAddressFormattedNewLine1().length()==0) {
											holder.Lblitem2cs.setText(separated[0].trim());
										}
										holder.txt_type.setText("");
										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
									} else {


										if (separated[0].length() == 0) {
											holder.txt_type.setText("");
											holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
										//	holder.Lblitem2cs.setText("N/A");
										} else {
											if (separated[0].contains("Home")) {
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
												holder.txt_type.setText("H");
												holder.txt_type.setTextColor(Color.WHITE);
											} else if ((separated[0].contains("Business"))) {
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
												holder.txt_type.setText("B");
												holder.txt_type.setTextColor(Color.WHITE);

											} else if ((separated[0].contains("Government"))) {
												holder.txt_type.setText("G");
												holder.txt_type.setTextColor(Color.WHITE);
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
											} else {
												if ((separated[0].contains("Not a physical Address"))) {
													holder.txt_type.setText("NPA");
													holder.txt_type.setTextColor(Color.WHITE);
													holder.txt_type.setTextSize(10);
													holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
												} else if ((separated[0].contains("Other"))) {
													holder.txt_type.setText("O");
													holder.txt_type.setTextColor(Color.WHITE);
													holder.txt_type.setTextSize(10);
													holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
												}
											}
										}

										//if(separated.length>1){
										if (separated[1].length() == 0) {
										//	holder.Lblitem2cs.setText("N/A");
										} else {
											if (separated[1].trim().length() != 0) {
												if(object.getAddressFormattedNewLine1().length()==0) {
												holder.Lblitem2cs.setText(separated[1].trim());
												}
//									holder.Lblitem2cs.setTextColor(Color.WHITE);
											} else {
										//		holder.Lblitem2cs.setText("");
											}
										}
									}
								}
							}

							if (object.getServee() == null || object.getServee().length() == 0) {
								holder.textWide.setText("");
//					holder.textWide.setTextColor(Color.BLACK);

							} else {
								holder.textWide.setText(object.getServee());
//					holder.textWide.setTextColor(Color.BLACK);
							}

							String Due_Date = object.getDueDate();
							Time_separated = Due_Date.split("T");
							if (Time_separated.length != 0) {
								String date = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);
								holder.txt_due_date.setText(date.trim());
//					holder.txt_due_date.setTextColor(Color.BLACK);

							} else {
								holder.txt_due_date.setText("");

							}

							if (object.isHasAttachments()) {
								holder.attach.setVisibility(View.VISIBLE);
							} else {
								holder.attach.setVisibility(View.GONE);
							}

							holder.txt_priority_title.setTextColor(activity.getResources().getColor(R.color.whitecolor));
							holder.Lblitem1cs.setTextColor(activity.getResources().getColor(R.color.whitecolor));
							holder.txt_status.setTextColor(activity.getResources().getColor(R.color.whitecolor));
							holder.Lblitem2cs.setTextColor(activity.getResources().getColor(R.color.whitecolor));
							holder.textWide.setTextColor(activity.getResources().getColor(R.color.whitecolor));
							holder.txt_due_date.setTextColor(activity.getResources().getColor(R.color.whitecolor));


						} else if (!Standard_priority) {
							boolean Special_priority = Pattern.compile(Pattern.quote("Special"), Pattern.CASE_INSENSITIVE)
									.matcher(object.getPriorityTitle()).find();
							if (true) {


								if (object.getPriorityTitle() == null || object.getPriorityTitle().length() == 0) {
									holder.txt_priority_title.setText("");
								} else {
									holder.txt_priority_title.setText(object.getPriorityTitle());
								}

								if (object.getWorkorder() == null || object.getWorkorder().length() == 0) {
									holder.Lblitem1cs.setText("");
								} else {
									holder.Lblitem1cs.setText(object.getWorkorder());
								}

								String Status_title = object.getMilestoneTitle();

								if (Status_title.length() == 0) {
									holder.txt_status.setText("");
								} else {
									holder.txt_status.setText(Status_title);

								}

								String Address = object.getAddressFormattedForDisplay();
								if(object.getAddressFormattedNewLine1().length()!=0) {
									holder.Lblitem2cs.setText(object.getAddressFormattedNewLine1() + "\n" + object.getAddressFormattedNewLine2());
								}
								if (Address != null || Address.length() != 0) {
									separated = Address.split(":");
									if (separated.length == 0) {
										if(object.getAddressFormattedNewLine1().length()==0) {
											holder.Lblitem2cs.setText(Address);
										}
										holder.txt_type.setText("");
										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));

									} else {
										if (separated.length == 1) {
											if(object.getAddressFormattedNewLine1().length()==0) {
												holder.Lblitem2cs.setText(separated[0].trim());
											}
											holder.txt_type.setText("");
											holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
										} else {


											if (separated[0].length() == 0) {
												holder.txt_type.setText("");
												holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
												//holder.Lblitem2cs.setText("N/A");
											} else {
												if (separated[0].contains("Home")) {
													holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
													holder.txt_type.setText("H");
													holder.txt_type.setTextColor(Color.WHITE);
												} else if ((separated[0].contains("Business"))) {
													holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
													holder.txt_type.setText("B");
													holder.txt_type.setTextColor(Color.WHITE);

												} else if ((separated[0].contains("Government"))) {
													holder.txt_type.setText("G");
													holder.txt_type.setTextColor(Color.WHITE);
													holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
												} else {
													if ((separated[0].contains("Not a physical Address"))) {
														holder.txt_type.setText("NPA");
														holder.txt_type.setTextColor(Color.WHITE);
														holder.txt_type.setTextSize(10);
														holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
													} else if ((separated[0].contains("Other"))) {
														holder.txt_type.setText("O");
														holder.txt_type.setTextColor(Color.WHITE);
														holder.txt_type.setTextSize(10);
														holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
													}
												}
											}

											//if(separated.length>1){
											if (separated[1].length() == 0) {
											//	holder.Lblitem2cs.setText("N/A");
											} else {
												if (separated[1].trim().length() != 0) {
													if(object.getAddressFormattedNewLine1().length()==0) {
															holder.Lblitem2cs.setText(separated[1].trim());
													}
//									holder.Lblitem2cs.setTextColor(Color.WHITE);
												} else {
												//	holder.Lblitem2cs.setText("");
												}
											}
										}
									}
								}

								if (object.getServee() == null || object.getServee().length() == 0) {
									holder.textWide.setText("");
//					holder.textWide.setTextColor(Color.BLACK);

								} else {
									holder.textWide.setText(object.getServee());
//					holder.textWide.setTextColor(Color.BLACK);
								}

								String Due_Date = object.getDueDate();
								Time_separated = Due_Date.split("T");
								if (Time_separated.length != 0) {
									String date = changeDateFormat("yyy-MM-dd","MM/dd/yyyy",Time_separated[0]);
									holder.txt_due_date.setText(date.trim());
//					holder.txt_due_date.setTextColor(Color.BLACK);

								} else {
									holder.txt_due_date.setText("");

								}

								if (object.isHasAttachments()) {
									holder.attach.setVisibility(View.VISIBLE);
								} else {
									holder.attach.setVisibility(View.GONE);
								}

								holder.txt_priority_title.setTextColor(activity.getResources().getColor(R.color.purple));
								holder.Lblitem1cs.setTextColor(activity.getResources().getColor(R.color.purple));
								holder.txt_status.setTextColor(activity.getResources().getColor(R.color.purple));
								holder.Lblitem2cs.setTextColor(activity.getResources().getColor(R.color.purple));
								holder.textWide.setTextColor(activity.getResources().getColor(R.color.purple));
								holder.txt_due_date.setTextColor(activity.getResources().getColor(R.color.purple));


							}

						}
					}

				}

//				if (object.getWorkorder() == null || object.getWorkorder().length() == 0) {
//					holder.Lblitem1cs.setText("");
//					holder.Lblitem1cs.setTextColor(Color.WHITE);
//				} else {
//					holder.Lblitem1cs.setText(object.getWorkorder());
//					holder.Lblitem1cs.setTextColor(Color.WHITE);
//				}
//
//				String Address = object.getAddressFormattedForDisplay();
//				if (Address != null || Address.length() != 0) {
//					separated = Address.split(":");
//
//					if (separated.length == 0) {
//						holder.Lblitem2cs.setText(Address);
//						holder.txt_type.setText("");
//						holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
//
//					} else {
//
//						if (separated.length == 1) {
//							holder.Lblitem2cs.setText(separated[0].trim());
//							holder.txt_type.setText("");
//							holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
//						} else {
//
//
//							if (separated[0].length() == 0) {
//								holder.txt_type.setText("");
//								holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
//								holder.Lblitem2cs.setText("N/A");
//							} else {
//								if (separated[0].contains("Home")) {
//									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
//									holder.txt_type.setText("H");
//									holder.txt_type.setTextColor(Color.WHITE);
//								} else if ((separated[0].contains("Business"))) {
//									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
//									holder.txt_type.setText("B");
//									holder.txt_type.setTextColor(Color.WHITE);
//
//								} else if ((separated[0].contains("Government"))) {
//									holder.txt_type.setText("G");
//									holder.txt_type.setTextColor(Color.WHITE);
//									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
//								} else {
//									if ((separated[0].contains("Not a physical Address"))) {
//										holder.txt_type.setText("NPA");
//										holder.txt_type.setTextColor(Color.WHITE);
//										holder.txt_type.setTextSize(10);
//										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
//									} else if ((separated[0].contains("Other"))) {
//										holder.txt_type.setText("O");
//										holder.txt_type.setTextColor(Color.WHITE);
//										holder.txt_type.setTextSize(10);
//										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_box_background));
//									}
//								}
//							}
//
//							//if(separated.length>1){
//							if (separated[1].length() == 0) {
//								holder.Lblitem2cs.setText("N/A");
//							} else {
//								if (separated[1].trim().length() != 0) {
//									holder.Lblitem2cs.setText(separated[1].trim());
//									holder.Lblitem2cs.setTextColor(Color.WHITE);
//								} else {
//									holder.Lblitem2cs.setText("");
//								}
//
//							}
//
//
//						}
//					}
//
//				}
//			}
//
//			if (object.getServee() == null || object.getServee().length() == 0) {
//				holder.textWide.setText("");
//				holder.textWide.setTextColor(Color.WHITE);
//			} else {
//				holder.textWide.setText(object.getServee());
//				holder.textWide.setTextColor(Color.WHITE);
//
////				String Address = object.getAddressFormattedForDisplay();
////				separated = Address.split(":");
////
////				if (separated[0] == null || separated[0].length() ==0){
////					holder.txt_type.setText("");
////					holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
////				}
////				else
////					{
////					boolean addresstype = Pattern.compile(Pattern.quote("Home"), Pattern.CASE_INSENSITIVE)
////							.matcher(separated[0]).find();
////					if (addresstype) {
////						holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.greenbackground));
////						holder.txt_type.setText("H");
////						holder.txt_type.setTextColor(Color.WHITE);
////					}
////
////					else
////						{
////						boolean addresstype1 = Pattern.compile(Pattern.quote("Business"), Pattern.CASE_INSENSITIVE)
////								.matcher(separated[0]).find();
////						if (addresstype1) {
////							holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
////							holder.txt_type.setText("B");
////							holder.txt_type.setTextColor(Color.WHITE);
////						} else {
////
////							boolean addresstype2 = Pattern.compile(Pattern.quote("Government"), Pattern.CASE_INSENSITIVE)
////									.matcher(separated[0]).find();
////							if (addresstype2) {
////								holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
////								holder.txt_type.setText("G");
////								holder.txt_type.setTextColor(Color.WHITE);
////							}
////							else{
////								boolean addresstype3 = Pattern.compile(Pattern.quote("Not a physical Address"), Pattern.CASE_INSENSITIVE)
////										.matcher(separated[0]).find();
////								if (addresstype3) {
////									holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
////									holder.txt_type.setText("NPA");
////									holder.txt_type.setTextColor(Color.WHITE);
////									holder.txt_type.setTextSize(10);
////								}else {
////									boolean addresstype4 = Pattern.compile(Pattern.quote("Other"), Pattern.CASE_INSENSITIVE)
////											.matcher(separated[0]).find();
////									if (addresstype4) {
////										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.bluebackground));
////										holder.txt_type.setText("O");
////										holder.txt_type.setTextColor(Color.WHITE);
////									}else {
////										holder.txt_type.setText("");
////										holder.txt_type_parent.setBackground(activity.getDrawable(R.drawable.black_background));
////									}
////								}
////							}
////						}
////					}
////				}
////
////				if (separated[1].trim().length() != 0) {
////					holder.Lblitem2cs.setText(separated[1].trim());
////					holder.Lblitem2cs.setTextColor(Color.BLACK);
////				} else {
////					holder.Lblitem2cs.setText("");
////				}
//
//				String Due_Date = object.getDueDate();
//				Time_separated = Due_Date.split("T");
//
//				if (Time_separated.length != 0) {
//					holder.txt_due_date.setText(Time_separated[0].trim());
//					holder.txt_due_date.setTextColor(Color.WHITE);
//				} else {
//					holder.txt_due_date.setText("");
//				}
//
//				if (object.isHasAttachments()) {
//					holder.attach.setVisibility(View.VISIBLE);
//				} else {
//					holder.attach.setVisibility(View.GONE);
//				}
//
//			}

			}
////
			if (object.getMilestoneTitle() != null && object.getMilestoneTitle().length() != 0) {
				String Title = object.getMilestoneTitle();

//				boolean Status_priority_Hold = Pattern.compile(Pattern.quote("Hold"), Pattern.CASE_INSENSITIVE)
//						.matcher(object.getMilestoneTitle()).find();
//				if (Status_priority_Hold){
//					holder.ListView_Processorder.setBackground(activity.getDrawable(R.drawable.blue_background));
//					holder.list_navig.setImageResource(R.drawable.ic_navigation_next_item);
//					holder.attach.setImageResource(R.drawable.attach_white);
//				}else {
//					if (!Status_priority_Hold){
						boolean Status_priority_Cancelled =
								Pattern.compile(Pattern.quote("Cancelled"), Pattern.CASE_INSENSITIVE)
								.matcher(object.getMilestoneTitle()).find();
						if (Status_priority_Cancelled) {
							holder.ListView_Processorder.setBackground(activity.getDrawable(R.drawable.blue_background));
							holder.list_navig.setImageResource(R.drawable.ic_navigation_next_item);
							holder.attach.setImageResource(R.drawable.attach_white);
//						}
//					}
				}
			}


			holder.check.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					detail = (ProcessAddressForServer) cb.getTag();
					Log.d("Clicked on Checkbox", "" + cb.getText());
					detail.setSelected(cb.isChecked());

				}
			});

			holder.check.setChecked(object.isSelected());

			holder.check.setChecked(object.isSelected());
			holder.check.setTag(object);

			Log.d("selected", "" + object.isSelected());


			if (mShouldShowDragAndDropIcon) {
//					holder.Lblitem2cs.setText(R.string.fontello_drag_and_drop);
//					holder.textWide.setText(R.string.fontello_drag_and_drop);
//						holder.Lblitem1cs.setText(R.string.fontello_drag_and_drop);
			} else {

			}


			Log.d("sereeeeeeeeeeee", "" + object.getServee());
			Log.d("position", "" + holder.id);
		}
		return convertView;
	}

	private String changeDateFormat(String currentFormat,String requiredFormat,String dateString) {

		String result="";

		SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
		SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
		Date date=null;
		try {
			date = formatterOld.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			result = formatterNew.format(date);
		}
		return result;
	}

	public void onClick(View v) {

	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {

	}

	@Override
	public void onDismiss(ViewGroup arg0, int[] arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getUndoClickView(View arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getUndoView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static class ViewHolder {

		public TextView Lblitem1cs;
		public TextView Lblitem2cs;
		public TextView status;
		public TextView textWide;
		public TextView txt_priority_title;
		public TextView txt_due_date;
		public TextView txt_type;
				public TextView txt_status;
		public CheckBox check;
		public LinearLayout list_table;
		public LinearLayout ListView_Processorder;
		public LinearLayout txt_type_parent;
		public ImageView attach, list_navig;
		int id;

	}

	@SuppressWarnings("unused")
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		@Override
		public void onClick(DialogInterface arg0, int arg1) {

		}
	}
}