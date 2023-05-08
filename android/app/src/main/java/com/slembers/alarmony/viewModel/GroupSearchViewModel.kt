package com.slembers.alarmony.viewModel

import androidx.compose.runtime.mutableStateListOf
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
    private val _members : MutableList<MemberDto> = mutableStateListOf(),
    private val _CheckedMember : MutableLiveData<MutableList<MemberDto>> =
        MutableLiveData<MutableList<MemberDto>>(mutableListOf())
) : ViewModel() {

    val searchMembers : LiveData<List<MemberDto>> = _SearchMember
    val checkedMembers : LiveData<MutableList<MemberDto>> = _CheckedMember

    fun searchApi(
        keyword : String,
        groupId : String? = null,
    ) {
        GroupService.searchMember(
            keyword, groupId
        ) { _SearchMember.value = it?.memberList as MutableList<MemberDto>? }
    }

    fun addCurrentMember(member : MemberDto) {
        _members.add(member)
        _CheckedMember.postValue(_members)
    }

    fun removeCheckedMember(member : MemberDto) {
        _members.remove(member)
        _CheckedMember.postValue(_members)
    }

}