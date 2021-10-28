#import <Flutter/Flutter.h>
#import <shared/shared.h>

@interface PluginCodelabPlugin : NSObject<FlutterPlugin, SharedCommonMethodChannelResult>

@property (nonatomic, strong) id<SharedCommonMethodChannelResult> channelResult;

@end
