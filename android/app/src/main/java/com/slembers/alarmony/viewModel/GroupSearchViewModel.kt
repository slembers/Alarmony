package com.slembers.alarmony.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.GroupService

class GroupSearchViewModel(
    private val _SearchMember: MutableLiveData<List<MemberDto>> =
        MutableLiveData<List<MemberDto>>(mutableListOf()),
    private val _members : MutableList<Member> = mutableStateListOf(),
    private val _CheckedMember : MutableLiveData<MutableList<Member>> =
        MutableLiveData<MutableList<Member>>(mutableListOf()),
    private var _curMember : MutableLiveData<Member> = MutableLiveData<Member>(null),
    private val _found : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
) : ViewModel() {

    val searchMembers : LiveData<List<MemberDto>> = _SearchMember
    val checkedMembers : LiveData<MutableList<Member>>
        get()  = _CheckedMember
    val curMember : LiveData<Member> = _curMember
    val found : LiveData<Boolean> = _found

    fun searchApi(
        keyword : String,
        groupId : String? = null,
    ) {
        if (keyword.isEmpty() || keyword.trim().isEmpty()) return
        GroupService.searchMember(
            keyword, groupId
        ) { _SearchMember.value = it?.memberList as MutableList<MemberDto>? }
    }

    fun addCheckedMember(member : Member) {
        _members.add(member)
        _CheckedMember.postValue(_members)
    }

    fun removeCheckedMember(member : Member) {
        _members.remove(member)
        _CheckedMember.postValue(_members)
    }

    fun selectTarget(member : Member) : Member {
        _curMember.postValue(member)
        return member
    }

    fun setFound(bool : Boolean) {
        _found.postValue(bool)
    }

}