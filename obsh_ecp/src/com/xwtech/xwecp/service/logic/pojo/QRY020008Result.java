package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MusicClubMember;

public class QRY020008Result extends BaseServiceInvocationResult
{
	private List<MusicClubMember> musicClubMember = new ArrayList<MusicClubMember>();

	public void setMusicClubMember(List<MusicClubMember> musicClubMember)
	{
		this.musicClubMember = musicClubMember;
	}

	public List<MusicClubMember> getMusicClubMember()
	{
		return this.musicClubMember;
	}

}