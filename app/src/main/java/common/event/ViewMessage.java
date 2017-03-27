/*
 *
 * @author yandeqing
 * @created 2016.5.29
 * @email 18612205027@163.com
 * @version $version
 *
 */

package common.event;



/**
 * 更新UI视图元素的事件消息对象封装
 *
 * @author 桑龙佳
 */
public class ViewMessage extends BaseMessage implements ViewEventConster {

    public ViewMessage() {
        super();
    }

    public ViewMessage(int event, Object data) {
        super(event, data);
    }

    @Override
    public String toString() {
        return "ViewMessage [event=" + event + ", data=" + data + "]";
    }

}
