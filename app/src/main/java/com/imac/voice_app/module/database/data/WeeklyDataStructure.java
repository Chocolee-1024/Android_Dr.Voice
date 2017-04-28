package com.imac.voice_app.module.database.data;

public class WeeklyDataStructure {
        private String soundTopic = null;
        private String date = null;
        private String soundTopicPoint = "0";
        private String weeklyTopicPoint = null;

        public void setDate(String date) {
            this.date = date;
        }

        public void setSoundTopicPoint(String soundTopicPoint) {
            if (null != soundTopicPoint) this.soundTopicPoint = soundTopicPoint;
        }

        public void setWeeklyTopicPoint(String weeklyTopicPoint) {
            this.weeklyTopicPoint = weeklyTopicPoint;
        }

        public void setSoundTopic(String soundTopic) {
            this.soundTopic = soundTopic;
        }

        public String getDate() {
            return date;
        }

        public String getSoundTopicPoint() {
            return soundTopicPoint;
        }

        public String getWeeklyTopicPoint() {
            return weeklyTopicPoint;
        }

        public String getSoundTopic() {
            return soundTopic;
        }
    }