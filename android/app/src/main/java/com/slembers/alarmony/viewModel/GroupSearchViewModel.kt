package com.slembers.alarmony.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.network.service.GroupService

class GroupSearchViewModel(
    private val _SearchMember: MutableLiveData<List<MemberDto>> =
        MutableLiveData<List<MemberDto>>(mutableListOf()),
    private val _CurrentMember: MutableLiveData<List<Member>> =
        MutableLiveData<List<Member>>(mutableListOf())
) : ViewModel() {

    val searchMembers : LiveData<List<MemberDto>> = _SearchMember
    val currentMembers : LiveData<List<Member>> = _CurrentMember

    fun searchApi(
        keyword : String,
        groupId : String? = null,
    ) {
        GroupService.searchMember(
            keyword, groupId
        ) { _SearchMember.value = it?.memberList as MutableList<MemberDto>? }
    }



}