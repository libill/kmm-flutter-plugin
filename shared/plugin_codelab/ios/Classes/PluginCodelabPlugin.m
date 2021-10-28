#import "PluginCodelabPlugin.h"

@implementation PluginCodelabPlugin{
  int _numKeysDown;
  FlutterResult _flutterResult;
  SharedCommonCodelabPlugin* _codelabPlugin;
}

- (instancetype)init {
  self = [super init];
  if (self) {
    // create music
    _codelabPlugin = [[SharedCommonCodelabPlugin alloc] init];
  }
  return self;
}

- (void)dealloc {
    // destroy music
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName: SharedPluginCodeLabKt.PLUGIN_CODE_LAB_CHANNEL
            binaryMessenger:[registrar messenger]];
  PluginCodelabPlugin* instance = [[PluginCodelabPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall *)call
                  result:(FlutterResult)result {
    SharedCommonMethodCall *methodCall = [[SharedCommonMethodCall alloc] initWithMethod:call.method arguments:call.arguments];
    _flutterResult = result;
    [_codelabPlugin onMethodCallCall:methodCall result:self ];
}

- (void)errorErrorCode:(NSString * _Nullable)errorCode errorMessage:(NSString * _Nullable)errorMessage errorDetails:(id _Nullable)errorDetails {
    NSError *error = [NSError errorWithDomain:NSCocoaErrorDomain code:errorCode.intValue userInfo:@{@"errorMessage":errorMessage, @"errorDetails":errorDetails}];
    if (_flutterResult) {
        _flutterResult(error);
    }
}

- (void)notImplemented {
    if (_flutterResult) {
        _flutterResult(FlutterMethodNotImplemented);
    }
}

- (void)successResult:(id _Nullable)result {
    if (_flutterResult) {
        _flutterResult(result);
    }
}

@end
