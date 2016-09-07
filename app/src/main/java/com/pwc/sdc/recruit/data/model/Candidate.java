package com.pwc.sdc.recruit.data.model;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:dongpo 创建时间: 7/15/2016
 * 描述:
 * 修改:
 */
public class Candidate extends ParcelEntity {

    public String candidateId;
    public Basic basic;
    public Position position;
    public List<Experience> workExperiences;
    public String language;
    public long submitTime;
    public String headUrl;

    public static class Experience extends ParcelEntity{
        public String companyName;
        public String from;
        public String to;
        public String companyPhone;
        public String position;
        public String leaveReason;
        public Contact referee;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.companyName);
            dest.writeString(this.from);
            dest.writeString(this.to);
            dest.writeString(this.companyPhone);
            dest.writeString(this.position);
            dest.writeString(this.leaveReason);
            dest.writeParcelable(this.referee, flags);
        }

        public Experience() {
        }

        protected Experience(Parcel in) {
            this.companyName = in.readString();
            this.from = in.readString();
            this.to = in.readString();
            this.companyPhone = in.readString();
            this.position = in.readString();
            this.leaveReason = in.readString();
            this.referee = in.readParcelable(Contact.class.getClassLoader());
        }

        public static final Creator<Experience> CREATOR = new Creator<Experience>() {
            @Override
            public Experience createFromParcel(Parcel source) {
                return new Experience(source);
            }

            @Override
            public Experience[] newArray(int size) {
                return new Experience[size];
            }
        };
    }

    public static class Position extends ParcelEntity{
        public String applyFor;
        public String experience;
        public String recruitChannel;
        public String availableDate;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.applyFor);
            dest.writeString(this.experience);
            dest.writeString(this.recruitChannel);
            dest.writeString(this.availableDate);
        }

        public Position() {
        }

        protected Position(Parcel in) {
            this.applyFor = in.readString();
            this.experience = in.readString();
            this.recruitChannel = in.readString();
            this.availableDate = in.readString();
        }

        public static final Creator<Position> CREATOR = new Creator<Position>() {
            @Override
            public Position createFromParcel(Parcel source) {
                return new Position(source);
            }

            @Override
            public Position[] newArray(int size) {
                return new Position[size];
            }
        };
    }

    public static class Basic extends ParcelEntity{
        public String chineseName;
        public String englishName;
        public String gender;
        public String married;
        public String passportNo;
        public String country;
        public String identityNo;
        public String birth;
        public String academicDegree;
        public String graduateSchool;
        public String graduateDate;
        public LanguageLevel languageLevel;
        public String major;
        public String email;
        public String mobile;
        public String telephone;
        public String currentAddress;
        public String postCode;
        public Contact emergency;
        public String medicalHistory;
        public String employeeReferral;
        public String hasFriendsInPWC;
        public String interests;
        public String takeService;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.chineseName);
            dest.writeString(this.englishName);
            dest.writeString(this.gender);
            dest.writeString(this.married);
            dest.writeString(this.passportNo);
            dest.writeString(this.country);
            dest.writeString(this.identityNo);
            dest.writeString(this.birth);
            dest.writeString(this.academicDegree);
            dest.writeString(this.graduateSchool);
            dest.writeString(this.graduateDate);
            dest.writeParcelable(this.languageLevel, flags);
            dest.writeString(this.major);
            dest.writeString(this.email);
            dest.writeString(this.mobile);
            dest.writeString(this.telephone);
            dest.writeString(this.currentAddress);
            dest.writeString(this.postCode);
            dest.writeParcelable(this.emergency, flags);
            dest.writeString(this.medicalHistory);
            dest.writeString(this.employeeReferral);
            dest.writeString(this.hasFriendsInPWC);
            dest.writeString(this.interests);
            dest.writeString(this.takeService);
        }

        public Basic() {
        }

        protected Basic(Parcel in) {
            this.chineseName = in.readString();
            this.englishName = in.readString();
            this.gender = in.readString();
            this.married = in.readString();
            this.passportNo = in.readString();
            this.country = in.readString();
            this.identityNo = in.readString();
            this.birth = in.readString();
            this.academicDegree = in.readString();
            this.graduateSchool = in.readString();
            this.graduateDate = in.readString();
            this.languageLevel = in.readParcelable(LanguageLevel.class.getClassLoader());
            this.major = in.readString();
            this.email = in.readString();
            this.mobile = in.readString();
            this.telephone = in.readString();
            this.currentAddress = in.readString();
            this.postCode = in.readString();
            this.emergency = in.readParcelable(Contact.class.getClassLoader());
            this.medicalHistory = in.readString();
            this.employeeReferral = in.readString();
            this.hasFriendsInPWC = in.readString();
            this.interests = in.readString();
            this.takeService = in.readString();
        }

        public static final Creator<Basic> CREATOR = new Creator<Basic>() {
            @Override
            public Basic createFromParcel(Parcel source) {
                return new Basic(source);
            }

            @Override
            public Basic[] newArray(int size) {
                return new Basic[size];
            }
        };
    }

    public static class Contact extends ParcelEntity{
        public Contact(String name, String relationship, String phone){
            this.name = name;
            this.relationship = relationship;
            this.phone = phone;
        }

        public String name;
        public String relationship;
        public String phone;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.relationship);
            dest.writeString(this.phone);
        }

        protected Contact(Parcel in) {
            this.name = in.readString();
            this.relationship = in.readString();
            this.phone = in.readString();
        }

        public static final Creator<Contact> CREATOR = new Creator<Contact>() {
            @Override
            public Contact createFromParcel(Parcel source) {
                return new Contact(source);
            }

            @Override
            public Contact[] newArray(int size) {
                return new Contact[size];
            }
        };
    }

    public static class LanguageLevel extends ParcelEntity{
        public String english;
        public String jspanese;
        public String others;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.english);
            dest.writeString(this.jspanese);
            dest.writeString(this.others);
        }

        public LanguageLevel() {
        }

        protected LanguageLevel(Parcel in) {
            this.english = in.readString();
            this.jspanese = in.readString();
            this.others = in.readString();
        }

        public static final Creator<LanguageLevel> CREATOR = new Creator<LanguageLevel>() {
            @Override
            public LanguageLevel createFromParcel(Parcel source) {
                return new LanguageLevel(source);
            }

            @Override
            public LanguageLevel[] newArray(int size) {
                return new LanguageLevel[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.candidateId);
        dest.writeParcelable(this.basic, flags);
        dest.writeParcelable(this.position, flags);
        dest.writeTypedList(this.workExperiences);
        dest.writeString(this.language);
        dest.writeLong(this.submitTime);
        dest.writeString(this.headUrl);
    }

    public Candidate() {
    }

    protected Candidate(Parcel in) {
        this.candidateId = in.readString();
        this.basic = in.readParcelable(Basic.class.getClassLoader());
        this.position = in.readParcelable(Position.class.getClassLoader());
        this.workExperiences = in.createTypedArrayList(Experience.CREATOR);
        this.language = in.readString();
        this.submitTime = in.readLong();
        this.headUrl = in.readString();
    }

    public static final Creator<Candidate> CREATOR = new Creator<Candidate>() {
        @Override
        public Candidate createFromParcel(Parcel source) {
            return new Candidate(source);
        }

        @Override
        public Candidate[] newArray(int size) {
            return new Candidate[size];
        }
    };
}
