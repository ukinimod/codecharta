"use strict";

import {TreeViewDirective} from "./treeViewDirective.js";
import {TreeViewNodeDirective} from "./treeViewNodeDirective.js";
import "../common/checkbox/checkbox";

angular.module(
    "app.codeCharta.ui.treeView",
    ["ivh.treeview", "app.codeCharta.ui.common.checkbox"]
);

angular.module("app.codeCharta.ui.treeView").directive(
    "treeViewDirective",
    () => new TreeViewDirective()
);

angular.module("app.codeCharta.ui.treeView").directive(
    "treeViewNodeDirective",
    () => new TreeViewNodeDirective()
);

