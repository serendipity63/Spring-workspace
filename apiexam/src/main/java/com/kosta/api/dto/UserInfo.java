package com.kosta.api.dto;

public class UserInfo {
	private String id;
	private String nickname;
	private String profileImage;
	private String thumbnailImage;
	private String email;
	private String gender;

	private String name;
	private String mobile;
	private String age;
	private String birthday;
	private String address;
	private String password;

	public UserInfo() { // 기본생성자가 있어야 한다
	}

	public UserInfo(String id, String nickname, String profileImage, String thumbnailImage, String email,
			String gender) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.thumbnailImage = thumbnailImage;
		this.email = email;
		this.gender = gender;
	}

	public UserInfo(String id, String nickname, String email, String gender, String name, String mobile, String age,
			String birthday) { // naver용
		super();
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.gender = gender;
		this.name = name;
		this.mobile = mobile;
		this.age = age;
		this.birthday = birthday;
	}

	public UserInfo(String id, String nickname, String profileImage, String thumbnailImage, String email, String name,
			String mobile, String address, String password) { // 일반 회원가입 위한 생성자
		super();
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.thumbnailImage = thumbnailImage;
		this.email = email;
		this.name = name;
		this.mobile = mobile;
		this.address = address;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
