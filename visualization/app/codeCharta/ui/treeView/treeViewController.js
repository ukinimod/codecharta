"use strict";
/**
 * Controls the treeView
 */
class TreeViewController {

    /* @ngInject */

    /**
     * @constructor
     * @param {Scope} $scope
     * @param {DataService} dataService
     * @listens {data-changed}
     */
    constructor($scope, settingsService, ivhTreeviewMgr) {

        this.tree = settingsService.settings.map;

        let ctx = this;
        $scope.$on("settings-changed", (e,s)=>{this.tree = s.map;});

        this.treeOptions = {
            labelAttribute: "name",
            childrenAttribute: "children",
            useCheckboxes: true,
            defaultSelectedState: false,
            validate:true,
            expandToDepth: 1,
            twistieCollapsedTpl: "<i class='fa fa-folder fa-lg'></i>",
            twistieExpandedTpl: "<i class='fa fa-folder-open fa-lg'></i>",
            twistieLeafTpl: "<i class='fa fa-file fa-lg'></i>"
        };

        ivhTreeviewMgr.selectAll(this.tree);
        settingsService.onSettingsChanged();

    }

}

export {TreeViewController};


