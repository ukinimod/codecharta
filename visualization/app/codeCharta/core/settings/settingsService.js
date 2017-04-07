"use strict";

import {Settings} from "./model/settings";
import {Range} from "./model/range";

/**
 * Stores and manipulates the current settings
 */ 
class SettingsService {

    /* ngInject */

    /**
     * @constructor 
     * @param {UrlService} urlService 
     * @param {DataService} dataService
     * @param {Scope} $rootScope 
     */
    constructor(urlService, dataService, $rootScope) {

        /**
         * @type {UrlService}
         */
        this.urlService = urlService;

        /**
         * @type {DataService}
         */
        this.dataService = dataService;

        /**
         * @type {Scope}
         */
        this.rootScope = $rootScope;

       /**
        * @type {Settings}
        */
        this.settings = new Settings(
            dataService.data.currentmap,
            new Range(10,20,false),
            dataService.data.metrics[0],
            dataService.data.metrics[1],
            dataService.data.metrics[2],
            true,
            false
        );

        let ctx = this;

        $rootScope.$on("data-changed", (event,data) => {
           ctx.onDataChanged(data);
        });

    }

    /**
     * if the parameters property metrics is longer than 2 elements then change the map and metric settings according to the parameter. 
     * @listens {data-changed} called on data-changed
     * @emits {settings-changed} transitively on call
     * @param {DataModel} data
     */
    onDataChanged(data) {

        if(data.metrics.length >= 3){
            this.settings.map = data.currentmap;

            if(data.metrics.indexOf(this.settings.areaMetric) === -1){
                //area metric is not set or not in the new metrics and needs to be chosen
                this.settings.areaMetric = data.metrics[0];
            }

            if(data.metrics.indexOf(this.settings.heightMetric) === -1){
                //height metric is not set or not in the new metrics and needs to be chosen
                this.settings.heightMetric = data.metrics[1];
            }

            if(data.metrics.indexOf(this.settings.colorMetric) === -1){
                //color metric is not set or not in the new metrics and needs to be chosen
                this.settings.colorMetric = data.metrics[2];
            }

        }

        this.onSettingsChanged();

    }

    /**
     * Broadcasts a settings-changed event with the new {Settings} object as a payload
     * @emits {settings-changed} on call
     */
    onSettingsChanged() {
        this.rootScope.$broadcast("settings-changed", this.settings);
    }

    /**
     * updates the settings object according to url parameters.
     * @emits {settings-changed} transitively on call
     */
    updateSettingsFromUrl() {

        //for every property in settings...
        for (var property in this.settings) {

            //...that is not inherited from object...
            if (this.settings.hasOwnProperty(property) && property !== "map") {

                //...get the query param value for the property...
                var val = this.urlService.getParam(property);

                if (val && val.length > 0) {

                    //if it exists, set ist the settings value
                    this.settings[property] = val;
                }
            }
        }

        //TODO map some special keys like neutralColorRange

        this.onSettingsChanged();

    }

}

export {SettingsService};