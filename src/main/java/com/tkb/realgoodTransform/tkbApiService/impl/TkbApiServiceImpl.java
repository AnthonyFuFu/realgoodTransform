package com.tkb.realgoodTransform.tkbApiService.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkb.api.client.TKBAPIClient;
import com.tkb.api.client.model.TKBAPIRequestModel;
import com.tkb.api.client.model.TKBAPIResponseModel;
import com.tkb.api.client.util.Security;
import com.tkb.realgoodTransform.model.User;
import com.tkb.realgoodTransform.model.UserAccount;
import com.tkb.realgoodTransform.service.UserAccountService;
import com.tkb.realgoodTransform.tkbApiService.TkbApiService;

@Service
public class TkbApiServiceImpl implements TkbApiService {
	private final String reqEncryptKey = "tkb-api-req-xxxx";
	private final String repDecryptKey = "tkb-api-rep-xxxx";
	private final String appName = "ERP";
	
	@Autowired
	private UserAccountService userAccountService;

	//撈全部人基本資訊,新增至table:user_account(初始化),新增至資料庫
	@SuppressWarnings("rawtypes")
	@Override
	public void UserMasterList() throws Exception {
		List<Map> list = new ArrayList<Map>();

		TKBAPIRequestModel tTKBAPIRequestModel = new TKBAPIRequestModel();
		tTKBAPIRequestModel.setAppName(appName);
		tTKBAPIRequestModel.setService(3);
		tTKBAPIRequestModel.setMethod(5);
		tTKBAPIRequestModel.setVersion(1);
		tTKBAPIRequestModel.setDataList(list);

		TKBAPIClient tTKPAPIClient = new TKBAPIClient();

		try {
			String rep = tTKPAPIClient.sendRequest(tTKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel tResponse = new TKBAPIResponseModel(rep);

			if (tResponse.getStatus() == 1) {
				//寫入使用者清單方法,尚未做檢查使用者是否存在的判斷
				if (tResponse.getDataList() != null && tResponse.getDataList().size() > 0) {
					try {
						tResponse.getDataList().stream().forEach( e -> {
							UserAccount userAccount = new UserAccount();
							userAccount.setEmployee_no(e.get("EMPLOYEE_NO") == null ? "" : String.valueOf(e.get("EMPLOYEE_NO")));
							userAccount.setEmployee_name(String.valueOf(e.get("CHINESE_NAME")));
							userAccount.setDepartment_id(e.get("DEPARTMENT") == null ? "" : String.valueOf(e.get("DEPARTMENT")));
							userAccount.setUnit_id(e.get("UNIT_NO") == null ? "" : String.valueOf(e.get("UNIT_NO")));
							userAccount.setEmail(e.get("EMAIL") == null ? "" : String.valueOf(e.get("EMAIL")));
							userAccount.setCreate_by("admin");
							//檢查員工是否已在資料庫內，有:略過，無:新增
							User user = new User();
							user.setAccount(userAccount.getEmployee_no());
							User userData = userAccountService.getDataByAccount(user);
							if(userData == null) {
								userAccountService.insert(userAccount);
							}
						});
						System.out.println("successful to insert userAccount.");
					}catch (Exception e1) {
						System.out.println("Failed to insert userAccount.");
						e1.printStackTrace();
						throw new Exception();
					}
				} else {
					System.out.println("使用者清單為空");
				}
			} else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JSONException(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> departmentList(UserAccount userAccount, String appName, String reqEncryptKey,
			String repDecryptKey) {
		Map map = new HashMap<>();
		map.put("COMPANY_NO", userAccount.getCompany());
		List<Map> list = new ArrayList<>();
		list.add(map);
		
		TKBAPIRequestModel tTKBAPIRequestModel = new TKBAPIRequestModel();
		tTKBAPIRequestModel.setAppName(appName);
		tTKBAPIRequestModel.setService(3);
		tTKBAPIRequestModel.setMethod(3);
		tTKBAPIRequestModel.setVersion(1);
		tTKBAPIRequestModel.setDataList(list);
		
		TKBAPIClient tTKPAPIClient = new TKBAPIClient();
		try {
			String rep = tTKPAPIClient.sendRequest(tTKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel tResponse = new TKBAPIResponseModel(rep);
			
			if(tResponse.getStatus() == 1) {
				if(tResponse.getDataList() != null && tResponse.getDataList().size() > 0) {
					return tResponse.getDataList();
				} else {
					return null;
				}
			} else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> unitList(UserAccount userAccount, String appName, String reqEncryptKey, String repDecryptKey) {
		Map map = new HashMap<>();
		map.put("DEPARTMENT_NO", userAccount.getDepartment_no());
		
		List<Map> list = new ArrayList<>();
		list.add(map);
		
		TKBAPIRequestModel tTKBAPIRequestModel = new TKBAPIRequestModel();
		tTKBAPIRequestModel.setAppName(appName);
		tTKBAPIRequestModel.setService(3);
		tTKBAPIRequestModel.setMethod(4);
		tTKBAPIRequestModel.setVersion(1);
		tTKBAPIRequestModel.setDataList(list);
		
		TKBAPIClient tTKPAPIClient = new TKBAPIClient();
		try {
			String rep = tTKPAPIClient.sendRequest(tTKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel tResponse = new TKBAPIResponseModel(rep);
			
			if(tResponse.getStatus() == 1) {
				if(tResponse.getDataList() != null && tResponse.getDataList().size() > 0) {
					return tResponse.getDataList();
				} else {
					return null;
				}
			} else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map UserMasterList(User user) {
		Map map = new HashMap();
		map.put("LOGIN_ID", user.getAccount());
		map.put("LOGIN_PASSWD", user.getPassword());
		
		List<Map> list = new ArrayList<>();
		list.add(map);
		
		TKBAPIRequestModel tTKBAPIRequestModel = new TKBAPIRequestModel();
		tTKBAPIRequestModel.setAppName(appName);
		tTKBAPIRequestModel.setService(3);
		tTKBAPIRequestModel.setMethod(1);
		tTKBAPIRequestModel.setVersion(1);
		tTKBAPIRequestModel.setDataList(list);
		
		TKBAPIClient tTKBAPIClient = new TKBAPIClient();
		
		try {
			String rep = tTKBAPIClient.sendRequest(tTKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel tResponse = new TKBAPIResponseModel(rep);
				
			if(tResponse.getStatus() == 1) {
				if(tResponse.getDataList() != null && tResponse.getDataList().size() > 0) {
					return tResponse.getDataList().get(0);
				} else {
					return null;
				}
			} else {	
//				log.error("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());	
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + tResponse.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> newUserList(LocalDate beginDate) {
		List<Map> list = new ArrayList<>();
		String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate);
		String yesterday = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate.minusDays(1));
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("BEGIN_DATE", yesterday);
		map.put("END_DATE", today);
		list.add(map);
		
		TKBAPIRequestModel tKBAPIRequestModel = new TKBAPIRequestModel();
		tKBAPIRequestModel.setAppName(appName);
		tKBAPIRequestModel.setService(3);
		tKBAPIRequestModel.setMethod(14);
		tKBAPIRequestModel.setVersion(2);
		tKBAPIRequestModel.setDataList(list);
		
		TKBAPIClient tKBAPIClient = new TKBAPIClient();
		
		try {
			String rep = tKBAPIClient.sendRequest(tKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel response = new TKBAPIResponseModel(rep);
			if (response.getStatus() == 1) {
				if(response.getDataList().size() > 0) {
					return response.getDataList();
				}else {
					return null;
				}
			}else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + response.getMessage());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> leaveUser(LocalDate beginDate) {
		List<Map> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate);
		String yesterday = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate.minusDays(1));
		map.put("BEGIN_DATE", yesterday);
		map.put("END_DATE", today);
		list.add(map);
		
		TKBAPIRequestModel tKBAPIRequestModel = new TKBAPIRequestModel();
		tKBAPIRequestModel.setAppName(appName);
		tKBAPIRequestModel.setService(3);
		tKBAPIRequestModel.setMethod(13);
		tKBAPIRequestModel.setVersion(2);
		tKBAPIRequestModel.setDataList(list);
		
		TKBAPIClient tKBAPIClient = new TKBAPIClient();
		
		try {
			String rep = tKBAPIClient.sendRequest(tKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel response = new TKBAPIResponseModel(rep);
			if(response.getStatus() == 1) {
				if(response.getDataList().size() > 0) {
					return response.getDataList();
				}else {
					return null;
				}
			}else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + response.getMessage());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getModifiedUserList(LocalDate beginDate) {
		List<Map> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate);
		String yesterday = DateTimeFormatter.ofPattern("yyyyMMdd").format(beginDate.minusDays(1));
		map.put("BEGIN_DATE", yesterday);
		map.put("END_DATE", today);
		list.add(map);

		TKBAPIRequestModel tKBAPIRequestModel = new TKBAPIRequestModel();
		tKBAPIRequestModel.setAppName(appName);
		tKBAPIRequestModel.setService(3);
		tKBAPIRequestModel.setMethod(15);
		tKBAPIRequestModel.setVersion(2);
		tKBAPIRequestModel.setDataList(list);

		TKBAPIClient tKBAPIClient = new TKBAPIClient();

		try {
			String rep = tKBAPIClient.sendRequest(tKBAPIRequestModel, reqEncryptKey);
			rep = Security.decrypt(rep, repDecryptKey);
			TKBAPIResponseModel response = new TKBAPIResponseModel(rep);
			if (response.getStatus() == 1) {
				if (response.getDataList().size() > 0) {
					return response.getDataList();
				} else {
					return null;
				}
			} else {
				System.out.println("取資料時出現錯誤 api 回傳錯誤訊息" + response.getMessage());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
