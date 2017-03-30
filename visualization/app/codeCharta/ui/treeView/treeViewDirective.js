"use strict";

import {TreeViewController} from "./treeViewController.js";

/**
 * Renders a treeview of the current map. Allows the user to select and filter nodes and branches.
 */
class TreeViewDirective {

    /**
     * @constructor
     */
    constructor() {
        /**
         *
         * @type {string}
         */
        this.templateUrl = "./treeView.html";

        /**
         *
         * @type {string}
         */
        this.restrict = "E";

        /**
         *
         * @type {Scope}
         */
        this.scope = {};

        /**
         *
         * @type {SettingsPanelController}
         */
        this.controller = TreeViewController;

        /**
         *
         * @type {string}
         */
        this.controllerAs = "ctrl";

        /**
         *
         * @type {boolean}
         */
        this.bindToController = true;
    }
    
}

export {TreeViewDirective};

