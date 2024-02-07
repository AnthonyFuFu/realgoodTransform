package com.tkb.realgoodTransform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tkb.realgoodTransform.dao.GroupUserDao;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.service.GroupUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupUserServiceImpl implements GroupUserService {
	
	private final GroupUserDao groupUserDao;
	
	@Override
	public void add(GroupUser groupUser) {
		groupUserDao.add(groupUser);
	}

	@Override
	public void update(GroupUser groupUser) {
		groupUserDao.update(groupUser);
	}

	@Override
	public Integer checkUser(GroupUser groupUser) {
		return groupUserDao.checkUser(groupUser);
	}

	@Override
	public Integer getGroupId(Integer id) {
		return groupUserDao.getGroupId(id);
	}

	@Override
	public List<GroupUser> getData(GroupUser groupUser) {
		List<GroupUser> groupData = new ArrayList<>();
		try {
			groupData = groupUserDao.getData(groupUser);
		} catch (Exception e) {
			System.err.println(e);
		}
		return groupData;
	}

}
