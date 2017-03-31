"use strict";

import {TreeViewController} from "./treeViewController.js";

/**
 * Renders a node of the treeview
 */
class TreeViewNodeDirective {

    /**
     * @constructor
     */
    constructor(ivhTreeviewMgr) {

        this.mgr = ivhTreeviewMgr;

        this.require = "^ivhTreeview";

        /**
         *
         * @type {string}
         */
        this.templateUrl = "./treeViewNode.html";

        /**
         *
         * @type {string}
         */
        this.restrict = "AE";

        /**
         *
         * @type {Scope}
         */
        this.scope = {};

        /**
         *
         * @type {TreeViewController}
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

    link (scope, element, attrs) {
        element.on("click", function() {
            this.mgr.select(this.controller.tree, scope.node, !scope.node.selected);
            scope.$apply();
        });
    }
    
}

export {TreeViewNodeDirective};
