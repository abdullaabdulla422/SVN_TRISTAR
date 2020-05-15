package com.tristar.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tristar.object.AddressForServer;
import com.tristar.object.CourtAddressForServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class EntryAdapter extends ArrayAdapter<Object> {

	Context mContext;
	private ArrayList<Object> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context, ArrayList<Object> items) {
		super(context, 0, items);
		this.items = items;
		mContext = context;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		Object item = items.get(position);
		if (item instanceof String) {
			v = vi.inflate(R.layout.list_item_section, null);
			TextView sectionView = (TextView) v.findViewById(R.id.title);
			sectionView.setText(item.toString());
		} else {
			v = vi.inflate(R.layout.list_item_enty, null);
			LinearLayout Layout_Court_Delivery_jobs = (LinearLayout) v.findViewById(R.id.Layout_Court_Delivery_jobs);
			TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
			TextView subtitle = (TextView) v.findViewById(R.id.list_item_entry_summary);
			TextView subtitle2 = (TextView) v.findViewById(R.id.list_item_entry_summary2);
			TextView receive_date = (TextView) v.findViewById(R.id.list_item_receive_date);
			ImageView imgAttach = (ImageView) v.findViewById(R.id.img_attach);
			TextView list_item_currrent_status = (TextView) v.findViewById(R.id.list_item_currrent_status);
			TextView list_item_priority = (TextView) v.findViewById(R.id.list_item_priority);
			ImageView img_forward = (ImageView) v.findViewById(R.id.img_forward);
			//TextView list_item_entry_name = (TextView) v.findViewById(R.id.list_item_entry_name);

			if (item instanceof CourtAddressForServer) {

				CourtAddressForServer courtAddress = (CourtAddressForServer) item;

				String Check_Today_Priority = courtAddress.getPriorityTitle();
				boolean Today_priority = Pattern.compile(Pattern.quote("Today"), Pattern.CASE_INSENSITIVE)
						.matcher(courtAddress.getPriorityTitle()).find();

				if (Today_priority) {
					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.white_background));
					img_forward.setImageResource(R.drawable.navigation_next_item_black);

					title.setText(courtAddress.getWorkorder() + " - " + courtAddress.getServeeName());

					Log.d("ServeeName1", "" + courtAddress.getServeeName());

					Log.d("Court_date", "" + courtAddress.getDueDate());

					//subtitle.setText(courtAddress.getAddressFormattedForDisplay());

					String str = courtAddress.getAddressFormattedForDisplay();
					String str1 = null;
					Log.d("Subtitle_1", "" + str.toString());
					Log.d("Has Attachment", "" + courtAddress.isHasAttachments());

					String Receive_date = courtAddress.getDueDate();
					if(courtAddress.getAddressFormattedNewLine1().length()!=0){
						subtitle.setText(courtAddress.getAddressFormattedNewLine1());
						subtitle2.setText(courtAddress.getAddressFormattedNewLine2());
					}

					if (courtAddress.isHasAttachments()) {
						imgAttach.setVisibility(View.VISIBLE);
						imgAttach.setImageResource(R.drawable.attach_black);
					} else {
						imgAttach.setVisibility(View.GONE);
					}
                for (int ii = 0; ii < Receive_date.length(); ii++) {

                    Character character = Receive_date.charAt(ii);

                    if (character.toString().equals("T")) {

                        Receive_date = Receive_date.substring(0,ii);

                        String inputPattern = "yyyy-MM-dd";
                        String outputPattern = "MM/dd/yyyy";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                        Date date = null;


                        try {
                            date = inputFormat.parse(Receive_date);
                            str1 = outputFormat.format(date);
							Receive_date = str1;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.d("Receive_date", "" + Receive_date);
                        break;
                    }
                }
					if(Receive_date!=null){
						if(Receive_date.length()!=0){
							receive_date.setText(Receive_date.substring(0,10));
						}
					}

//					receive_date.setText(Receive_date);


					if (str != null || str.length() != 0) {
						for (int j = 0; j < str.length(); j++) {
							Character character = str.charAt(j);
							if (character.toString().equals("&")) {
								str = str.substring(j + 2);
								if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(str);
								}
//                            Log.d("Subtitle_1&",""+subtitle.getText().toString());

								String[] splitList = str.split(",");

								String Street_Address;
								List<String> SplitAray = new ArrayList<>();

								Street_Address = splitList[0].trim();
								String LineCity = splitList[1].trim();
								String State_Zip = splitList[2].trim();


								Log.d("Subtitle_1&", "" + subtitle.getText().toString());
								if(courtAddress.getAddressFormattedNewLine1().length()==0) {
									subtitle2.setText(LineCity + ", " + State_Zip);
								}
								Log.d("Subtitle2_1&", "" + subtitle2.getText().toString());

								break;
							} else {
								if(courtAddress.getAddressFormattedNewLine1().length()==0) {
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										  subtitle.setText(courtAddress.getAddressFormattedForDisplay());
									}
								}
//                            Log.d("Subtitle_1",""+subtitle.getText().toString());

								String[] splitList = courtAddress.getAddressFormattedForDisplay().split(",");

								String Street_Address;
								List<String> SplitAray = new ArrayList<>();

								Street_Address = splitList[0].trim();
								String LineCity = splitList[1].trim();
								String State_Zip = splitList[2].trim();
								if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
								}
								Log.d("Subtitle_1", "" + subtitle.getText().toString());
								if(courtAddress.getAddressFormattedNewLine1().length()==0) {
									subtitle2.setText(LineCity + ", " + State_Zip);
								}
								Log.d("Subtitle2_1", "" + subtitle2.getText().toString());

							}
						}

						String Current_Status = courtAddress.getMilestoneTitle();
						if (Current_Status == null || Current_Status.length() == 0) {
							list_item_currrent_status.setVisibility(View.GONE);
						} else {
							list_item_currrent_status.setText(Current_Status);
						}
					}

					list_item_priority.setText(courtAddress.getPriorityTitle());

					title.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					subtitle.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					subtitle2.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					receive_date.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					list_item_priority.setTextColor(mContext.getResources().getColor(R.color.light_orange));


				} else {

					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.black_background));
					img_forward.setImageResource(R.drawable.ic_navigation_next_item);

					boolean Rush_priority = Pattern.compile(Pattern.quote("Rush"), Pattern.CASE_INSENSITIVE)
							.matcher(courtAddress.getPriorityTitle()).find();

					if (Rush_priority) {

						title.setText(courtAddress.getWorkorder() + " - " + courtAddress.getServeeName());

						Log.d("ServeeName1", "" + courtAddress.getServeeName());

						Log.d("Court_date", "" + courtAddress.getDueDate());
						if(courtAddress.getAddressFormattedNewLine1().length()!=0) {
							subtitle.setText(courtAddress.getAddressFormattedNewLine1());
							subtitle2.setText(courtAddress.getAddressFormattedNewLine2());
						}

						String str = courtAddress.getAddressFormattedForDisplay();
						String str1 = null;
						Log.d("Subtitle_1", "" + str.toString());
						Log.d("Has Attachment", "" + courtAddress.isHasAttachments());

						String Receive_date = courtAddress.getDueDate();
						if (courtAddress.isHasAttachments()) {
							imgAttach.setVisibility(View.VISIBLE);
						} else {
							imgAttach.setVisibility(View.GONE);
						}
                for (int ii = 0; ii < Receive_date.length(); ii++) {

                    Character character = Receive_date.charAt(ii);

                    if (character.toString().equals("T")) {

                        Receive_date = Receive_date.substring(0,ii);

                        String inputPattern = "yyyy-MM-dd";
                        String outputPattern = "MM/dd/yyyy";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                        Date date = null;


                        try {
                            date = inputFormat.parse(Receive_date);
                            str1 = outputFormat.format(date);
							Receive_date = str1;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.d("Receive_date", "" + Receive_date);
                        break;
                    }
                }

						if(Receive_date!=null){
							if(Receive_date.length()!=0){
								receive_date.setText(Receive_date.substring(0,10));
							}
						}

						if (str != null || str.length() != 0) {
							for (int j = 0; j < str.length(); j++) {
								Character character = str.charAt(j);
								if (character.toString().equals("&")) {
									str = str.substring(j + 2);
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(str);
									}
//                            Log.d("Subtitle_1&",""+subtitle.getText().toString());

									String[] splitList = str.split(",");

									String Street_Address;
									List<String> SplitAray = new ArrayList<>();

									Street_Address = splitList[0].trim();
									String LineCity = splitList[1].trim();
									String State_Zip = splitList[2].trim();
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
									}
									Log.d("Subtitle_1&", "" + subtitle.getText().toString());
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										subtitle2.setText(LineCity + ", " + State_Zip);
									}
									Log.d("Subtitle2_1&", "" + subtitle2.getText().toString());

									break;
								} else {
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(courtAddress.getAddressFormattedForDisplay());
									}
//                            Log.d("Subtitle_1",""+subtitle.getText().toString());

									String[] splitList = courtAddress.getAddressFormattedForDisplay().split(",");

									String Street_Address;
									List<String> SplitAray = new ArrayList<>();

									Street_Address = splitList[0].trim();
									String LineCity = splitList[1].trim();
									String State_Zip = splitList[2].trim();
									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
									}
									Log.d("Subtitle_1", "" + subtitle.getText().toString());

									if(courtAddress.getAddressFormattedNewLine1().length()==0) {
										subtitle2.setText(LineCity + ", " + State_Zip);
									}
									Log.d("Subtitle2_1", "" + subtitle2.getText().toString());

								}
							}

							String Current_Status = courtAddress.getMilestoneTitle();
							if (Current_Status == null || Current_Status.length() == 0) {
								list_item_currrent_status.setVisibility(View.GONE);
							} else {
								list_item_currrent_status.setText(Current_Status);
							}
						}
						list_item_priority.setText(courtAddress.getPriorityTitle());

						title.setTextColor(mContext.getResources().getColor(R.color.red));
						subtitle.setTextColor(mContext.getResources().getColor(R.color.red));
						subtitle2.setTextColor(mContext.getResources().getColor(R.color.red));
						receive_date.setTextColor(mContext.getResources().getColor(R.color.red));
						list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.red));
						list_item_priority.setTextColor(mContext.getResources().getColor(R.color.red));
					} else {
						if (!Rush_priority) {
							boolean Standard_priority = Pattern.compile(Pattern.quote("Standard"), Pattern.CASE_INSENSITIVE)
									.matcher(courtAddress.getPriorityTitle()).find();
							if (Standard_priority) {

								title.setText(courtAddress.getWorkorder() + " - " + courtAddress.getServeeName());

								Log.d("ServeeName1", "" + courtAddress.getServeeName());

								Log.d("Court_date", "" + courtAddress.getDueDate());
								if(courtAddress.getAddressFormattedNewLine1().length()!=0) {
									subtitle.setText(courtAddress.getAddressFormattedNewLine1());
									subtitle2.setText(courtAddress.getAddressFormattedNewLine2());
								}
								String str = courtAddress.getAddressFormattedForDisplay();
								String str1 = null;
								Log.d("Subtitle_1", "" + str.toString());
								Log.d("Has Attachment", "" + courtAddress.isHasAttachments());

								String Receive_date = courtAddress.getDueDate();
								if (courtAddress.isHasAttachments()) {
									imgAttach.setVisibility(View.VISIBLE);
								} else {
									imgAttach.setVisibility(View.GONE);
								}
                for (int ii = 0; ii < Receive_date.length(); ii++) {

                    Character character = Receive_date.charAt(ii);

                    if (character.toString().equals("T")) {

                        Receive_date = Receive_date.substring(0,ii);

                        String inputPattern = "yyyy-MM-dd";
                        String outputPattern = "MM/dd/yyyy";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                        Date date = null;


                        try {
                            date = inputFormat.parse(Receive_date);
                            str1 = outputFormat.format(date);
							Receive_date = str1;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.d("Receive_date", "" + str1);
                        break;
                    }
                }

								if(Receive_date!=null){
									if(Receive_date.length()!=0){
										receive_date.setText(Receive_date.substring(0,10));
									}
								}

								if (str != null || str.length() != 0) {
									for (int j = 0; j < str.length(); j++) {
										Character character = str.charAt(j);
										if (character.toString().equals("&")) {
											str = str.substring(j + 2);
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(str);
											}
//                            Log.d("Subtitle_1&",""+subtitle.getText().toString());

											String[] splitList = str.split(",");

											String Street_Address;
											List<String> SplitAray = new ArrayList<>();

											Street_Address = splitList[0].trim();
											String LineCity = splitList[1].trim();
											String State_Zip = splitList[2].trim();
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
												subtitle.setText(Street_Address);
											}
											Log.d("Subtitle_1&", "" + subtitle.getText().toString());
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
												subtitle2.setText(LineCity + ", " + State_Zip);
											}
											Log.d("Subtitle2_1&", "" + subtitle2.getText().toString());

											break;
										} else {
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(courtAddress.getAddressFormattedForDisplay());
											}
//                            Log.d("Subtitle_1",""+subtitle.getText().toString());

											String[] splitList = courtAddress.getAddressFormattedForDisplay().split(",");

											String Street_Address;
											List<String> SplitAray = new ArrayList<>();

											Street_Address = splitList[0].trim();
											String LineCity = splitList[1].trim();
											String State_Zip = splitList[2].trim();
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
												subtitle.setText(Street_Address);
											}
											Log.d("Subtitle_1", "" + subtitle.getText().toString());
											if(courtAddress.getAddressFormattedNewLine1().length()==0) {
												subtitle2.setText(LineCity + ", " + State_Zip);
											}
											Log.d("Subtitle2_1", "" + subtitle2.getText().toString());

										}
									}

									String Current_Status = courtAddress.getMilestoneTitle();
									if (Current_Status == null || Current_Status.length() == 0) {
										list_item_currrent_status.setVisibility(View.GONE);
									} else {
										list_item_currrent_status.setText(Current_Status);
									}
								}
								list_item_priority.setText(courtAddress.getPriorityTitle());

								title.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								subtitle.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								subtitle2.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								receive_date.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								list_item_priority.setTextColor(mContext.getResources().getColor(R.color.whitecolor));


							} else if (!Standard_priority) {
								boolean Special_priority = Pattern.compile(Pattern.quote("Special"), Pattern.CASE_INSENSITIVE)
										.matcher(courtAddress.getPriorityTitle()).find();
								if (Special_priority) {

									title.setText(courtAddress.getWorkorder() + " - " + courtAddress.getServeeName());

									Log.d("ServeeName1", "" + courtAddress.getServeeName());

									Log.d("Court_date", "" + courtAddress.getDueDate());
									if(courtAddress.getAddressFormattedNewLine1().length()!=0) {
										subtitle.setText(courtAddress.getAddressFormattedNewLine1());
										subtitle2.setText(courtAddress.getAddressFormattedNewLine2());
									}
									String str = courtAddress.getAddressFormattedForDisplay();
									String str1 = null;
									Log.d("Subtitle_1", "" + str.toString());
									Log.d("Has Attachment", "" + courtAddress.isHasAttachments());

									String Receive_date = courtAddress.getDueDate();
									if (courtAddress.isHasAttachments()) {
										imgAttach.setVisibility(View.VISIBLE);
									} else {
										imgAttach.setVisibility(View.GONE);
									}
                for (int ii = 0; ii < Receive_date.length(); ii++) {

                    Character character = Receive_date.charAt(ii);

                    if (character.toString().equals("T")) {

                        Receive_date = Receive_date.substring(0,ii);

                        String inputPattern = "yyyy-MM-dd";
                        String outputPattern = "MM/dd/yyyy";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                        Date date = null;


                        try {
                            date = inputFormat.parse(Receive_date);
                            str1 = outputFormat.format(date);
							Receive_date = str1;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.d("Receive_date", "" + Receive_date);
                        break;
                    }
                }

									if(Receive_date!=null){
										if(Receive_date.length()!=0){
											receive_date.setText(Receive_date.substring(0,10));
										}
									}

									if (str != null || str.length() != 0) {
										for (int j = 0; j < str.length(); j++) {
											Character character = str.charAt(j);
											if (character.toString().equals("&")) {
												str = str.substring(j + 2);
												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(str);
												}
//                            Log.d("Subtitle_1&",""+subtitle.getText().toString());

												String[] splitList = str.split(",");

												String Street_Address;
												List<String> SplitAray = new ArrayList<>();

												Street_Address = splitList[0].trim();
												String LineCity = splitList[1].trim();
												String State_Zip = splitList[2].trim();
												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
													subtitle.setText(Street_Address);
												}
												Log.d("Subtitle_1&", "" + subtitle.getText().toString());
												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
													subtitle2.setText(LineCity + ", " + State_Zip);
												}
												Log.d("Subtitle2_1&", "" + subtitle2.getText().toString());

												break;
											} else {
//												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
//                            subtitle.setText(courtAddress.getAddressFormattedForDisplay());
//												}
//                            Log.d("Subtitle_1",""+subtitle.getText().toString());

												String[] splitList = courtAddress.getAddressFormattedForDisplay().split(",");

												String Street_Address;
												List<String> SplitAray = new ArrayList<>();

												Street_Address = splitList[0].trim();
												String LineCity = splitList[1].trim();
												String State_Zip = splitList[2].trim();
												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
													subtitle.setText(Street_Address);
												}
												Log.d("Subtitle_1", "" + subtitle.getText().toString());
												if(courtAddress.getAddressFormattedNewLine1().length()==0) {
													subtitle2.setText(LineCity + ", " + State_Zip);
												}
												Log.d("Subtitle2_1", "" + subtitle2.getText().toString());

											}
										}

										String Current_Status = courtAddress.getMilestoneTitle();
										if (Current_Status == null || Current_Status.length() == 0) {
											list_item_currrent_status.setVisibility(View.GONE);
										} else {
											list_item_currrent_status.setText(Current_Status);
										}
									}

									list_item_priority.setText(courtAddress.getPriorityTitle());

									title.setTextColor(mContext.getResources().getColor(R.color.purple));
									subtitle.setTextColor(mContext.getResources().getColor(R.color.purple));
									subtitle2.setTextColor(mContext.getResources().getColor(R.color.purple));
									receive_date.setTextColor(mContext.getResources().getColor(R.color.purple));
									list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.purple));
									list_item_priority.setTextColor(mContext.getResources().getColor(R.color.purple));
								}
							}
						}

					}

				}

//				boolean Status_priority_Hold = Pattern.compile(Pattern.quote("Hold"), Pattern.CASE_INSENSITIVE)
//						.matcher(courtAddress.getMilestoneTitle()).find();
//				if (Status_priority_Hold) {
//					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.blue_background));
//					Log.d("Status_priority", "CourtAddressForServer = " + "Hold = " + "Blue");
//				} else {
//					if (!Status_priority_Hold) {
						boolean Status_priority_Cancelled = Pattern.compile(Pattern.quote("Cancelled"), Pattern.CASE_INSENSITIVE)
								.matcher(courtAddress.getMilestoneTitle()).find();
						if (Status_priority_Cancelled) {
							Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.blue_background));
							Log.d("Status_priority", "CourtAddressForServer = " + "Cancelled = " + "Blue");
//						}

//					}
				}

//
//				title.setText(courtAddress.getWorkorder() + " - " + courtAddress.getServeeName());
//
//				Log.d("ServeeName1", "" + courtAddress.getServeeName());
//
//				Log.d("Court_date", "" + courtAddress.getDueDate());
//				//subtitle.setText(courtAddress.getAddressFormattedForDisplay());
//
//				String str = courtAddress.getAddressFormattedForDisplay();
//				String str1 = null;
//				Log.d("Subtitle_1", "" + str.toString());
//				Log.d("Has Attachment", "" + courtAddress.isHasAttachments());
//
//				String Receive_date = courtAddress.getDateReceived();
//				if (courtAddress.isHasAttachments()) {
//					imgAttach.setVisibility(View.VISIBLE);
//				} else {
//					imgAttach.setVisibility(View.GONE);
//				}
////                for (int ii = 0; ii < Receive_date.length(); ii++) {
////
////                    Character character = Receive_date.charAt(ii);
////
////                    if (character.toString().equals("T")) {
////
////                        Receive_date = Receive_date.substring(0,ii);
////
////                        String inputPattern = "yyyy-MM-dd";
////                        String outputPattern = "MM/dd/yyyy";
////                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
////                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
////
////                        Date date = null;
////
////
////                        try {
////                            date = inputFormat.parse(Receive_date);
////                            str1 = outputFormat.format(date);
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////
////                        Log.d("Receive_date", "" + Receive_date);
////                        break;
////                    }
////                }
//
//				receive_date.setText(Receive_date);
//
//				if (str != null || str.length() != 0) {
//					for (int j = 0; j < str.length(); j++) {
//						Character character = str.charAt(j);
//						if (character.toString().equals("&")) {
//							str = str.substring(j + 2);
////                            subtitle.setText(str);
////                            Log.d("Subtitle_1&",""+subtitle.getText().toString());
//
//							String[] splitList = str.split(",");
//
//							String Street_Address;
//							List<String> SplitAray = new ArrayList<>();
//
//							Street_Address = splitList[0].trim();
//							String LineCity = splitList[1].trim();
//							String State_Zip = splitList[2].trim();
//
//							subtitle.setText(Street_Address);
//							Log.d("Subtitle_1&", "" + subtitle.getText().toString());
//
//							subtitle2.setText(LineCity + ", " + State_Zip);
//							Log.d("Subtitle2_1&", "" + subtitle2.getText().toString());
//
//							break;
//						} else {
////                            subtitle.setText(courtAddress.getAddressFormattedForDisplay());
////                            Log.d("Subtitle_1",""+subtitle.getText().toString());
//
//							String[] splitList = courtAddress.getAddressFormattedForDisplay().split(",");
//
//							String Street_Address;
//							List<String> SplitAray = new ArrayList<>();
//
//							Street_Address = splitList[0].trim();
//							String LineCity = splitList[1].trim();
//							String State_Zip = splitList[2].trim();
//
//							subtitle.setText(Street_Address);
//							Log.d("Subtitle_1", "" + subtitle.getText().toString());
//
//							subtitle2.setText(LineCity + ", " + State_Zip);
//							Log.d("Subtitle2_1", "" + subtitle2.getText().toString());
//
//						}
//					}
//
//					String Current_Status = courtAddress.getMilestoneTitle();
//					if (Current_Status == null || Current_Status.length() == 0) {
//						list_item_currrent_status.setVisibility(View.GONE);
//					} else {
//						list_item_currrent_status.setText(Current_Status);
//					}
//				}


			} else if (item instanceof AddressForServer) {
				//list_item_entry_name.setVisibility(View.GONE);

				AddressForServer address = (AddressForServer) item;

				String Check_Today_Priority = address.getPriorityTitle();
				boolean Today_priority = Pattern.compile(Pattern.quote("Today"), Pattern.CASE_INSENSITIVE)
						.matcher(address.getPriorityTitle()).find();

				if (Today_priority) {
					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.white_background));
					img_forward.setImageResource(R.drawable.navigation_next_item_black);

					title.setText(address.getWorkorder() + " - " + address.getServeeName());

					Log.d("ServeeName2", "" + address.getServeeName());

					Log.d("Address_date", "" + address.getDueDate());

					String Address_date = address.getDueDate();
					String str1 = "";

					for (int ii = 0; ii < Address_date.length(); ii++) {

						Character character = Address_date.charAt(ii);

						if (character.toString().equals("T")) {

							Address_date = Address_date.substring(0, ii);

							String inputPattern = "yyyy-MM-dd";
							String outputPattern = "dd/MM/yyyy";
							SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
							SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

							Date date = null;


							try {
								date = inputFormat.parse(Address_date);
								str1 = outputFormat.format(date);
								Address_date = str1;
							} catch (ParseException e) {
								e.printStackTrace();
							}

							Log.d("Address_date", "" + Address_date);
							break;
						}
					}

					receive_date.setText(Address_date);

					if (address.isHasAttachments()) {
						imgAttach.setVisibility(View.VISIBLE);
						imgAttach.setImageResource(R.drawable.attach_black);

					} else {
						imgAttach.setVisibility(View.GONE);
					}

				/*AddressForServer address = (AddressForServer) item;
				String str=address.getAddressFormattedForDisplay();

				int size = str.length();
				for(int i=0; i<str.length(); i++){

					Character character = str.charAt(i);

					if(character.toString().equals("&")){

						str = str.substring(i+1,size);
						break;
					}
				}
				Log.d("Substring",""+str);
				//list_item_entry_name.setVisibility(View.GONE);

				title.setText(address.getWorkorder()+" - "+address.getServeeName());

				subtitle.setText(str);*/


					String strr = address.getAddressFormattedForDisplay();
					if(address.getAddressFormattedNewLine1().length()!=0) {
						subtitle.setText(address.getAddressFormattedNewLine1());
						subtitle2.setText(address.getAddressFormattedNewLine2());
					}
					Log.d("Subtitle_21", "" + strr.toString());
					if (strr != null || strr.length() != 0) {
						for (int j = 0; j < strr.length(); j++) {
							Character character = strr.charAt(j);
							if (character.toString().equals("&")) {
								strr = strr.substring(j + 2);
								if(address.getAddressFormattedNewLine1().length()==0) {

                            subtitle.setText(strr);
								}
//                            Log.d("Subtitle_2&",""+subtitle.getText().toString());

								String[] splitList = strr.split(",");

								String Street_Address;
								List<String> SplitAray = new ArrayList<>();

								Street_Address = splitList[0].trim();
								String LineCity = splitList[1].trim();
								String State_Zip = splitList[2].trim();
								if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
								}
								Log.d("Subtitle_211&", "" + subtitle.getText().toString());
								if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle2.setText(LineCity + ", " + State_Zip);
								}
								Log.d("Subtitle2_2111&", "" + subtitle2.getText().toString());

								break;
							} else {
//                            subtitle.setText(address.getAddressFormattedForDisplay());
//                            Log.d("Subtitle_2",""+subtitle.getText().toString());

								String[] splitList = address.getAddressFormattedForDisplay().split(",");

								String Street_Address;
								List<String> SplitAray = new ArrayList<>();

								Street_Address = splitList[0].trim();
								String LineCity = splitList[1].trim();
								String State_Zip = "";
								if (splitList.length > 2) {

									State_Zip = splitList[2].trim();
								}
								if(address.getAddressFormattedNewLine1().length()==0) {
									subtitle.setText(Street_Address);
								}
								Log.d("Subtitle_21111", "" + subtitle.getText().toString());
								if(address.getAddressFormattedNewLine1().length()==0) {
									subtitle2.setText(LineCity + ", " + State_Zip);
								}
								Log.d("Subtitle2_2", "" + subtitle2.getText().toString());
							}
						}
					}

					String Current_Status = address.getMilestoneTitle();
					if (Current_Status == null || Current_Status.length() == 0) {
						list_item_currrent_status.setVisibility(View.GONE);
					} else {
						list_item_currrent_status.setText(Current_Status);
					}

					list_item_priority.setText(address.getPriorityTitle());

					title.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					subtitle.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					subtitle2.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					receive_date.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.light_orange));
					list_item_priority.setTextColor(mContext.getResources().getColor(R.color.light_orange));


				} else {

					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.black_background));
					img_forward.setImageResource(R.drawable.ic_navigation_next_item);


					boolean Rush_priority = Pattern.compile(Pattern.quote("Rush"), Pattern.CASE_INSENSITIVE)
							.matcher(address.getPriorityTitle()).find();

					if (Rush_priority) {

						title.setText(address.getWorkorder() + " - " + address.getServeeName());

						Log.d("ServeeName2", "" + address.getServeeName());

						Log.d("Address_date", "" + address.getDueDate());

						String Address_date = address.getDueDate();
						String str1 = "";

						for (int ii = 0; ii < Address_date.length(); ii++) {

							Character character = Address_date.charAt(ii);

							if (character.toString().equals("T")) {

								Address_date = Address_date.substring(0, ii);

								String inputPattern = "yyyy-MM-dd";
								String outputPattern = "dd/MM/yyyy";
								SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
								SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

								Date date = null;


								try {
									date = inputFormat.parse(Address_date);
									str1 = outputFormat.format(date);
									Address_date = str1;
								} catch (ParseException e) {
									e.printStackTrace();
								}

								Log.d("Address_date", "" + Address_date);
								break;
							}
						}

						receive_date.setText(Address_date);

						if (address.isHasAttachments()) {
							imgAttach.setVisibility(View.VISIBLE);

						} else {
							imgAttach.setVisibility(View.GONE);
						}

				/*AddressForServer address = (AddressForServer) item;
				String str=address.getAddressFormattedForDisplay();

				int size = str.length();
				for(int i=0; i<str.length(); i++){

					Character character = str.charAt(i);

					if(character.toString().equals("&")){

						str = str.substring(i+1,size);
						break;
					}
				}
				Log.d("Substring",""+str);
				//list_item_entry_name.setVisibility(View.GONE);

				title.setText(address.getWorkorder()+" - "+address.getServeeName());

				subtitle.setText(str);*/
						if(address.getAddressFormattedNewLine1().length()!=0) {
							subtitle.setText(address.getAddressFormattedNewLine1());
							subtitle2.setText(address.getAddressFormattedNewLine2());
						}
						String strr = address.getAddressFormattedForDisplay();
						Log.d("Subtitle_211111", "" + strr.toString());
						if (strr != null || strr.length() != 0) {
							for (int j = 0; j < strr.length(); j++) {
								Character character = strr.charAt(j);
								if (character.toString().equals("&")) {
									strr = strr.substring(j + 2);
									if(address.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(strr);
									}
//                            Log.d("Subtitle_2&",""+subtitle.getText().toString());

									String[] splitList = strr.split(",");

									String Street_Address;
									List<String> SplitAray = new ArrayList<>();

									Street_Address = splitList[0].trim();
									String LineCity = splitList[1].trim();
									String State_Zip = splitList[2].trim();
									if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
									}
									Log.d("Subtitle_2&", "" + subtitle.getText().toString());
									if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle2.setText(LineCity + ", " + State_Zip);
									}
									Log.d("Subtitle2_2&", "" + subtitle2.getText().toString());

									break;
								} else {
//                            subtitle.setText(address.getAddressFormattedForDisplay());
//                            Log.d("Subtitle_2",""+subtitle.getText().toString());

									String[] splitList = address.getAddressFormattedForDisplay().split(",");

									String Street_Address;
									List<String> SplitAray = new ArrayList<>();

									Street_Address = splitList[0].trim();
									String LineCity = splitList[1].trim();
									String State_Zip = "";
									if (splitList.length > 2) {

										State_Zip = splitList[2].trim();
									}
									if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle.setText(Street_Address);
									}
									Log.d("Subtitle_2111111", "" + subtitle.getText().toString());
									if(address.getAddressFormattedNewLine1().length()==0) {
										subtitle2.setText(LineCity + ", " + State_Zip);
									}
									Log.d("Subtitle2_222", "" + subtitle2.getText().toString());
								}
							}
						}

						String Current_Status = address.getMilestoneTitle();
						if (Current_Status == null || Current_Status.length() == 0) {
							list_item_currrent_status.setVisibility(View.GONE);
						} else {
							list_item_currrent_status.setText(Current_Status);
						}

						list_item_priority.setText(address.getPriorityTitle());

						title.setTextColor(mContext.getResources().getColor(R.color.red));
						subtitle.setTextColor(mContext.getResources().getColor(R.color.red));
						subtitle2.setTextColor(mContext.getResources().getColor(R.color.red));
						receive_date.setTextColor(mContext.getResources().getColor(R.color.red));
						list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.red));
						list_item_priority.setTextColor(mContext.getResources().getColor(R.color.red));

					} else {
						if (!Rush_priority) {
							boolean Standard_priority = Pattern.compile(Pattern.quote("Standard"), Pattern.CASE_INSENSITIVE)
									.matcher(address.getPriorityTitle()).find();
							if (Standard_priority) {


								title.setText(address.getWorkorder() + " - " + address.getServeeName());

								Log.d("ServeeName2", "" + address.getServeeName());

								Log.d("Address_date", "" + address.getDueDate());

								String Address_date = address.getDueDate();
								String str1 = "";

								for (int ii = 0; ii < Address_date.length(); ii++) {

									Character character = Address_date.charAt(ii);

									if (character.toString().equals("T")) {

										Address_date = Address_date.substring(0, ii);

										String inputPattern = "yyyy-MM-dd";
										String outputPattern = "dd/MM/yyyy";
										SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
										SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

										Date date = null;


										try {
											date = inputFormat.parse(Address_date);
											str1 = outputFormat.format(date);
											Address_date = str1;
										} catch (ParseException e) {
											e.printStackTrace();
										}

										Log.d("Address_date", "" + Address_date);
										break;
									}
								}

								receive_date.setText(Address_date);

								if (address.isHasAttachments()) {
									imgAttach.setVisibility(View.VISIBLE);

								} else {
									imgAttach.setVisibility(View.GONE);
								}

				/*AddressForServer address = (AddressForServer) item;
				String str=address.getAddressFormattedForDisplay();

				int size = str.length();
				for(int i=0; i<str.length(); i++){

					Character character = str.charAt(i);

					if(character.toString().equals("&")){

						str = str.substring(i+1,size);
						break;
					}
				}
				Log.d("Substring",""+str);
				//list_item_entry_name.setVisibility(View.GONE);

				title.setText(address.getWorkorder()+" - "+address.getServeeName());

				subtitle.setText(str);*/


								String strr = address.getAddressFormattedForDisplay();
								if(address.getAddressFormattedNewLine1().length()!=0) {
									subtitle.setText(address.getAddressFormattedNewLine1());
									subtitle2.setText(address.getAddressFormattedNewLine2());
								}
								Log.d("Subtitle_2222", "" + strr.toString());
								if (strr != null || strr.length() != 0) {
									for (int j = 0; j < strr.length(); j++) {
										Character character = strr.charAt(j);
										if (character.toString().equals("&")) {
											strr = strr.substring(j + 2);
											if(address.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(strr);
											}
//                            Log.d("Subtitle_2&",""+subtitle.getText().toString());

											String[] splitList = strr.split(",");

											String Street_Address;
											List<String> SplitAray = new ArrayList<>();

											Street_Address = splitList[0].trim();
											String LineCity = splitList[1].trim();
											String State_Zip = splitList[2].trim();
											if(address.getAddressFormattedNewLine1().length()==0) {
												subtitle.setText(Street_Address);
											}
											Log.d("Subtitle_2&", "" + subtitle.getText().toString());
											if(address.getAddressFormattedNewLine1().length()==0) {
												subtitle2.setText(LineCity + ", " + State_Zip);
											}
											Log.d("Subtitle2_2&", "" + subtitle2.getText().toString());

											break;
										} else {
//                            subtitle.setText(address.getAddressFormattedForDisplay());
//                            Log.d("Subtitle_2",""+subtitle.getText().toString());

											String[] splitList = address.getAddressFormattedForDisplay().split(",");

											String Street_Address;
											List<String> SplitAray = new ArrayList<>();

											Street_Address = splitList[0].trim();
											String LineCity = splitList[1].trim();
											String State_Zip = "";
											if (splitList.length > 2) {

												State_Zip = splitList[2].trim();
											}
											if(address.getAddressFormattedNewLine1().length()==0) {
												subtitle.setText(Street_Address);
											}
											Log.d("Subtitle_2333", "" + subtitle.getText().toString());
											if(address.getAddressFormattedNewLine1().length()==0) {
												subtitle2.setText(LineCity + ", " + State_Zip);
											}
											Log.d("Subtitle2_2", "" + subtitle2.getText().toString());
										}
									}
								}

								String Current_Status = address.getMilestoneTitle();
								if (Current_Status == null || Current_Status.length() == 0) {
									list_item_currrent_status.setVisibility(View.GONE);
								} else {
									list_item_currrent_status.setText(Current_Status);
								}

								list_item_priority.setText(address.getPriorityTitle());

								title.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								subtitle.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								subtitle2.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								receive_date.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.whitecolor));
								list_item_priority.setTextColor(mContext.getResources().getColor(R.color.whitecolor));


							} else if (!Standard_priority) {
								boolean Special_priority = Pattern.compile(Pattern.quote("Special"), Pattern.CASE_INSENSITIVE)
										.matcher(address.getPriorityTitle()).find();
								if (Special_priority) {


									title.setText(address.getWorkorder() + " - " + address.getServeeName());

									Log.d("ServeeName2", "" + address.getServeeName());

									Log.d("Address_date", "" + address.getDueDate());

									String Address_date = address.getDueDate();
									String str1 = "";

									for (int ii = 0; ii < Address_date.length(); ii++) {

										Character character = Address_date.charAt(ii);

										if (character.toString().equals("T")) {

											Address_date = Address_date.substring(0, ii);

											String inputPattern = "yyyy-MM-dd";
											String outputPattern = "dd/MM/yyyy";
											SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
											SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

											Date date = null;


											try {
												date = inputFormat.parse(Address_date);
												str1 = outputFormat.format(date);
												Address_date = str1;
											} catch (ParseException e) {
												e.printStackTrace();
											}

											Log.d("Address_date", "" + Address_date);
											break;
										}
									}

									receive_date.setText(Address_date);

									if (address.isHasAttachments()) {
										imgAttach.setVisibility(View.VISIBLE);

									} else {
										imgAttach.setVisibility(View.GONE);
									}

				/*AddressForServer address = (AddressForServer) item;
				String str=address.getAddressFormattedForDisplay();

				int size = str.length();
				for(int i=0; i<str.length(); i++){

					Character character = str.charAt(i);

					if(character.toString().equals("&")){

						str = str.substring(i+1,size);
						break;
					}
				}
				Log.d("Substring",""+str);
				//list_item_entry_name.setVisibility(View.GONE);

				title.setText(address.getWorkorder()+" - "+address.getServeeName());

				subtitle.setText(str);*/


									String strr = address.getAddressFormattedForDisplay();
									if(address.getAddressFormattedNewLine1().length()!=0) {
										subtitle.setText(address.getAddressFormattedNewLine1());
										subtitle2.setText(address.getAddressFormattedNewLine2());
									}
									Log.d("Subtitle_2444", "" + strr.toString());
									if (strr != null || strr.length() != 0) {
										for (int j = 0; j < strr.length(); j++) {
											Character character = strr.charAt(j);
											if (character.toString().equals("&")) {
												strr = strr.substring(j + 2);
												if(address.getAddressFormattedNewLine1().length()==0) {
                            subtitle.setText(strr);
												}
//                            Log.d("Subtitle_2&",""+subtitle.getText().toString());

												String[] splitList = strr.split(",");

												String Street_Address;
												List<String> SplitAray = new ArrayList<>();

												Street_Address = splitList[0].trim();
												String LineCity = splitList[1].trim();
												String State_Zip = splitList[2].trim();
												if(address.getAddressFormattedNewLine1().length()==0) {
													subtitle.setText(Street_Address);
												}
												Log.d("Subtitle_2&", "" + subtitle.getText().toString());
												if(address.getAddressFormattedNewLine1().length()==0) {
													subtitle2.setText(LineCity + ", " + State_Zip);
												}
												Log.d("Subtitle2_2&", "" + subtitle2.getText().toString());

												break;
											} else {
//                            subtitle.setText(address.getAddressFormattedForDisplay());
//                            Log.d("Subtitle_2",""+subtitle.getText().toString());

												String[] splitList = address.getAddressFormattedForDisplay().split(",");

												String Street_Address;
												List<String> SplitAray = new ArrayList<>();

												Street_Address = splitList[0].trim();
												String LineCity = splitList[1].trim();
												String State_Zip = "";
												if (splitList.length > 2) {

													State_Zip = splitList[2].trim();
												}
												if(address.getAddressFormattedNewLine1().length()==0) {
													subtitle.setText(Street_Address);
												}
												Log.d("Subtitle_2555", "" + subtitle.getText().toString());
												if(address.getAddressFormattedNewLine1().length()==0) {
													subtitle2.setText(LineCity + ", " + State_Zip);
												}
												Log.d("Subtitle2_2", "" + subtitle2.getText().toString());
											}
										}
									}

									String Current_Status = address.getMilestoneTitle();
									if (Current_Status == null || Current_Status.length() == 0) {
										list_item_currrent_status.setVisibility(View.GONE);
									} else {
										list_item_currrent_status.setText(Current_Status);
									}

									list_item_priority.setText(address.getPriorityTitle());

									title.setTextColor(mContext.getResources().getColor(R.color.purple));
									subtitle.setTextColor(mContext.getResources().getColor(R.color.purple));
									subtitle2.setTextColor(mContext.getResources().getColor(R.color.purple));
									receive_date.setTextColor(mContext.getResources().getColor(R.color.purple));
									list_item_currrent_status.setTextColor(mContext.getResources().getColor(R.color.purple));
									list_item_priority.setTextColor(mContext.getResources().getColor(R.color.purple));


								}


							}

						}


					}
				}


//				boolean Status_priority_Hold = Pattern.compile(Pattern.quote("Hold"), Pattern.CASE_INSENSITIVE)
//						.matcher(address.getMilestoneTitle()).find();
//				if (Status_priority_Hold) {
//					Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.blue_background));
//					Log.d("Status_priority", " = AddressForServer = " + "Hold = " + "Blue");
//				} else {
//					if (!Status_priority_Hold) {
						boolean Status_priority_Cancelled = Pattern.compile(Pattern.quote("Cancelled"), Pattern.CASE_INSENSITIVE)
								.matcher(address.getMilestoneTitle()).find();
						if (Status_priority_Cancelled) {
							Layout_Court_Delivery_jobs.setBackground(mContext.getDrawable(R.drawable.blue_background));
							Log.d("Status_priority", " = AddressForServer = " + "Cancelled = " + "Blue");

//						}

//					}
				}


			}
		}
		return v;
	}
}
