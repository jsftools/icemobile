/*
* Copyright 2004-2013 ICEsoft Technologies Canada Corp. (c)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions an
* limitations under the License.
*/

#import <UIKit/UIKit.h>

@class MainViewController;

@interface Preferences : UIViewController<UITextFieldDelegate, 
        UIPickerViewDelegate, UIPickerViewDataSource> {

    BOOL isFancy;
    UITextField* urlField;
    UITextField* emailField;
    UIPickerView* historyPicker;
    MainViewController *mainViewController;
    UIView *oldView;
    NSMutableArray *history;
    UIButton *doneButton;
    UIButton *goButton;
    UIButton *quitButton;
    UIButton *reloadButton;
    UIButton *clearButton;
    UIButton *openInButton;
}

- (IBAction) doDone;
- (IBAction) doGo;
- (IBAction) doQuit;
- (IBAction) doReload;
- (IBAction) doClear;
- (IBAction) doOpenIn;
- (void) update;
- (void) dismiss;
- (void) clearHistory;
- (void) addIfUnique:(NSString *) url;

@property (nonatomic) BOOL isFancy;
@property (nonatomic, retain) IBOutlet UITextField *urlField;
@property (nonatomic, retain) IBOutlet UITextField *emailField;
@property (nonatomic, retain) IBOutlet UIPickerView *historyPicker;
@property (nonatomic, retain) MainViewController *mainViewController;
@property (nonatomic, retain) UIView *oldView;
@property (nonatomic, retain) NSMutableArray *history;
@property (nonatomic, retain) IBOutlet UIButton *doneButton;
@property (nonatomic, retain) IBOutlet UIButton *goButton;
@property (nonatomic, retain) IBOutlet UIButton *quitButton;
@property (nonatomic, retain) IBOutlet UIButton *reloadButton;
@property (nonatomic, retain) IBOutlet UIButton *clearButton;
@property (nonatomic, retain) IBOutlet UIButton *openInButton;

@end
