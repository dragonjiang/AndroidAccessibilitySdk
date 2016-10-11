package com.demo.dragonjiang.accessilibility_sdk.core;

import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.Filter;

import java.util.Arrays;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:48
 * @description
 */
public class PurposeUiInfo {

    public ICommand command;
    public String input;//输入的文本
    public Filter[] featureFilters;
    public Filter[] purposeFilters;
    public Filter[] preFilters;//节点前的特征文字
    public Filter[] nextFilters;//节点后的特征文字


    public PurposeUiInfo() {
    }

    public static class Builder {
        private PurposeUiInfo ui;

        public Builder() {
            this.ui = new PurposeUiInfo();
        }

        public Builder setCommand(final ICommand cmd) {
            ui.command = cmd;
            return this;
        }

        public Builder setInputText(final String input) {
            ui.input = input;
            return this;
        }

        public Builder setFeatureFilters(final Filter... filters) {
            ui.featureFilters = filters;
            return this;
        }

        public Builder setFeatureFilters(final String... args) {
            ui.featureFilters = PurposeUiHelper.buildContentFilters(args);
            return this;
        }

        public Builder setPurposeFilters(final Filter... filters) {
            ui.purposeFilters = filters;
            return this;
        }

        public Builder setPurposeFilters(final String... args) {
            ui.purposeFilters = PurposeUiHelper.buildContentFilters(args);
            return this;
        }


        public Builder setPreFilters(final Filter... filters) {
            ui.preFilters = filters;
            return this;
        }

        public Builder setPreFilters(final String... args) {
            ui.preFilters = PurposeUiHelper.buildContentFilters(args);
            return this;
        }

        public Builder setNextFilters(final Filter... filters) {
            ui.nextFilters = filters;
            return this;
        }

        public Builder setNextFilters(final String... args) {
            ui.nextFilters = PurposeUiHelper.buildContentFilters(args);
            return this;
        }

        public PurposeUiInfo build() {
            return ui;
        }
    }

    @Override
    public String toString() {
        return "PurposeUiInfo{" +
                "command=" + command +
                ", input='" + input + '\'' +
                ", featureFilters=" + Arrays.toString(featureFilters) +
                ", purposeFilters=" + Arrays.toString(purposeFilters) +
                ", preFilters=" + Arrays.toString(preFilters) +
                ", nextFilters=" + Arrays.toString(nextFilters) +
                '}';
    }
}
