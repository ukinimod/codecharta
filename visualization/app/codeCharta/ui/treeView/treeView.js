"use strict";

import {TreeViewDirective} from "./treeViewDirective.js";
import {TreeViewCheckboxDirective} from "./treeViewCheckboxDirective.js";
import {TreeViewController} from "./treeViewController.js";

import "../common/checkbox/checkbox";

angular.module(
    "app.codeCharta.ui.treeView",
    ["ivh.treeview"]
);

angular.module("app.codeCharta.ui.treeView").controller(
    "treeViewController", TreeViewController
);

angular.module("app.codeCharta.ui.treeView").directive(
    "treeViewDirective",
    () => new TreeViewDirective()
);


angular.module("app.codeCharta.ui.treeView").directive(
    "treeViewCheckboxDirective",
    ["ivhTreeviewMgr", (a) => new TreeViewCheckboxDirective(a)]
);

