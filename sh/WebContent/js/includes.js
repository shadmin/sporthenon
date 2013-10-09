//------------------------------------------------------------------------------------------------//
// Script:livepipe.js
//------------------------------------------------------------------------------------------------//
/**
 * @author Ryan Johnson <http://syntacticx.com/>
 * @copyright 2008 PersonalGrid Corporation <http://personalgrid.com/>
 * @package LivePipe UI
 * @license MIT
 * @url http://livepipe.net/core
 * @require prototype.js
 */
if(typeof(Control) == 'undefined')
    Control = {};
    
var $proc = function(proc){
    return typeof(proc) == 'function' ? proc : function(){return proc;};
};

var $value = function(value){
    return typeof(value) == 'function' ? value() : value;
};

Object.Event = {
    extend: function(object){
        object._objectEventSetup = function(event_name){
            this._observers = this._observers || {};
            this._observers[event_name] = this._observers[event_name] || [];
        };
        object.observe = function(event_name,observer){
            if(typeof(event_name) == 'string' && typeof(observer) != 'undefined'){
                this._objectEventSetup(event_name);
                if(!this._observers[event_name].include(observer))
                    this._observers[event_name].push(observer);
            }else
                for(var e in event_name)
                    this.observe(e,event_name[e]);
        };
        object.stopObserving = function(event_name,observer){
            this._objectEventSetup(event_name);
            if(event_name && observer)
                this._observers[event_name] = this._observers[event_name].without(observer);
            else if(event_name)
                this._observers[event_name] = [];
            else
                this._observers = {};
        };
        object.observeOnce = function(event_name,outer_observer){
        	var inner_observer = null;
            inner_observer = function(){
                outer_observer.apply(this,arguments);
                this.stopObserving(event_name,inner_observer);
            }.bind(this);
            this._objectEventSetup(event_name);
            this._observers[event_name].push(inner_observer);
        };
        object.notify = function(event_name){
            this._objectEventSetup(event_name);
            var collected_return_values = [];
            var args = $A(arguments).slice(1);
            try{
                for(var i = 0; i < this._observers[event_name].length; ++i)
                    collected_return_values.push(this._observers[event_name][i].apply(this._observers[event_name][i],args) || null);
            }catch(e){
                if(e == $break)
                    return false;
                else
                    throw e;
            }
            return collected_return_values;
        };
        if(object.prototype){
            object.prototype._objectEventSetup = object._objectEventSetup;
            object.prototype.observe = object.observe;
            object.prototype.stopObserving = object.stopObserving;
            object.prototype.observeOnce = object.observeOnce;
            object.prototype.notify = function(event_name){
                if(object.notify){
                    var args = $A(arguments).slice(1);
                    args.unshift(this);
                    args.unshift(event_name);
                    object.notify.apply(object,args);
                }
                this._objectEventSetup(event_name);
                var args = $A(arguments).slice(1);
                var collected_return_values = [];
                try{
                    if(this.options && this.options[event_name] && typeof(this.options[event_name]) == 'function')
                        collected_return_values.push(this.options[event_name].apply(this,args) || null);
                    for(var i = 0; i < this._observers[event_name].length; ++i)
                        collected_return_values.push(this._observers[event_name][i].apply(this._observers[event_name][i],args) || null);
                }catch(e){
                    if(e == $break)
                        return false;
                    else
                        throw e;
                }
                return collected_return_values;
            };
        }
    }
};

/* Begin Core Extensions */

//Element.observeOnce
Element.addMethods({
    observeOnce: function(element,event_name,outer_callback){
    	var inner_callback = null;
        inner_callback = function(){
            outer_callback.apply(this,arguments);
            Element.stopObserving(element,event_name,inner_callback);
        };
        Element.observe(element,event_name,inner_callback);
    }
});

//mouse:wheel
(function(){
    function wheel(event){
        var delta=null, element, custom_event;
        // normalize the delta
        if (event.wheelDelta) { // IE & Opera
            delta = event.wheelDelta / 120;
        } else if (event.detail) { // W3C
            delta =- event.detail / 3;
        }
        if (!delta) { return; }
        element = Event.extend(event).target;
        element = Element.extend(element.nodeType === Node.TEXT_NODE ? element.parentNode : element);
        custom_event = element.fire('mouse:wheel',{ delta: delta });
        if (custom_event.stopped) {
            Event.stop(event);
            return false;
        }
    }
    document.observe('mousewheel',wheel);
    document.observe('DOMMouseScroll',wheel);
})();

/* End Core Extensions */

//from PrototypeUI
var IframeShim = Class.create({
    initialize: function() {
        this.element = new Element('iframe',{
            style: 'position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);display:none',
            src: 'javascript:void(0);',
            frameborder: 0 
        });
        $(document.body).insert(this.element);
    },
    hide: function() {
        this.element.hide();
        return this;
    },
    show: function() {
        this.element.show();
        return this;
    },
    positionUnder: function(element) {
    	var element = null;
    	element = $(element);
    	if (!element) {return;}
        var offset = element.cumulativeOffset();
        var dimensions = element.getDimensions();
        this.element.setStyle({
            left: offset[0] + 'px',
            top: offset[1] + 'px',
            width: dimensions.width + 'px',
            height: dimensions.height + 'px',
            zIndex: element.getStyle('zIndex') - 1
        }).show();
        return this;
    },
    setBounds: function(bounds) {
        for(prop in bounds)
            bounds[prop] += 'px';
        this.element.setStyle(bounds);
        return this;
    },
    destroy: function() {
        if(this.element)
            this.element.remove();
        return this;
    }
});
//------------------------------------------------------------------------------------------------//
// Script:effects.js
//------------------------------------------------------------------------------------------------//
//script.aculo.us effects.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

//Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//Contributors:
//Justin Palmer (http://encytemedia.com/)
//Mark Pilgrim (http://diveintomark.org/)
//Martin Bialasinki
//
//script.aculo.us is freely distributable under the terms of an MIT-style license.
//For details, see the script.aculo.us web site: http://script.aculo.us/

//converts rgb() and #xxx to #xxxxxx format,
//returns self (or first argument) if not convertable
String.prototype.parseColor = function() {
var color = '#';
if (this.slice(0,4) == 'rgb(') {
 var cols = this.slice(4,this.length-1).split(',');
 var i=0; do { color += parseInt(cols[i]).toColorPart();} while (++i<3);
} else {
 if (this.slice(0,1) == '#') {
   if (this.length==4) for(var i=1;i<4;i++) color += (this.charAt(i) + this.charAt(i)).toLowerCase();
   if (this.length==7) color = this.toLowerCase();
 }
}
return (color.length==7 ? color : (arguments[0] || this));
};
Element.collectTextNodes = function(element) {
return $A($(element).childNodes).collect( function(node) {
 return (node.nodeType==3 ? node.nodeValue :
   (node.hasChildNodes() ? Element.collectTextNodes(node) : ''));
}).flatten().join('');
};

Element.collectTextNodesIgnoreClass = function(element, className) {
return $A($(element).childNodes).collect( function(node) {
 return (node.nodeType==3 ? node.nodeValue :
   ((node.hasChildNodes() && !Element.hasClassName(node,className)) ?
     Element.collectTextNodesIgnoreClass(node, className) : ''));
}).flatten().join('');
};

Element.setContentZoom = function(element, percent) {
element = $(element);
element.setStyle({fontSize: (percent/100) + 'em'});
if (Prototype.Browser.WebKit) window.scrollBy(0,0);
return element;
};

Element.getInlineOpacity = function(element){
//return ($(element).style.opacity || ''=='');
return false; // WARNING
};

Element.forceRerendering = function(element) {
try {
 element = $(element);
 var n = document.createTextNode(' ');
 element.appendChild(n);
 element.removeChild(n);
} catch(e) { }
};
var Effect = {
_elementDoesNotExistError: {
 name: 'ElementDoesNotExistError',
 message: 'The specified DOM element does not exist, but is required for this effect to operate'
},
Transitions: {
 linear: Prototype.K,
 sinoidal: function(pos) {
   return (-Math.cos(pos*Math.PI)/2) + .5;
 },
 reverse: function(pos) {
   return 1-pos;
 },
 flicker: function(pos) {
   var pos = ((-Math.cos(pos*Math.PI)/4) + .75) + Math.random()/4;
   return pos > 1 ? 1 : pos;
 },
 wobble: function(pos) {
   return (-Math.cos(pos*Math.PI*(9*pos))/2) + .5;
 },
 pulse: function(pos, pulses) {
   return (-Math.cos((pos*((pulses||5)-.5)*2)*Math.PI)/2) + .5;
 },
 spring: function(pos) {
   return 1 - (Math.cos(pos * 4.5 * Math.PI) * Math.exp(-pos * 6));
 },
 none: function(pos) {
   return 0;
 },
 full: function(pos) {
   return 1;
 }
},
DefaultOptions: {
 duration:   1.0,   // seconds
 fps:        100,   // 100= assume 66fps max.
 sync:       false, // true for combining
 from:       0.0,
 to:         1.0,
 delay:      0.0,
 queue:      'parallel'
},
tagifyText: function(element) {
 var tagifyStyle = 'position:relative';
 if (Prototype.Browser.IE) tagifyStyle += ';zoom:1';

 element = $(element);
 $A(element.childNodes).each( function(child) {
   if (child.nodeType==3) {
     child.nodeValue.toArray().each( function(character) {
       element.insertBefore(
         new Element('span', {style: tagifyStyle}).update(
           character == ' ' ? String.fromCharCode(160) : character),
           child);
     });
     Element.remove(child);
   }
 });
},
multiple: function(element, effect) {
 var elements;
 if (((typeof element == 'object') ||
     Object.isFunction(element)) &&
    (element.length))
   elements = element;
 else
   elements = $(element).childNodes;

 var options = Object.extend({
   speed: 0.1,
   delay: 0.0
 }, arguments[2] || { });
 var masterDelay = options.delay;

 $A(elements).each( function(element, index) {
   new effect(element, Object.extend(options, { delay: index * options.speed + masterDelay }));
 });
},
PAIRS: {
 'slide':  ['SlideDown','SlideUp'],
 'blind':  ['BlindDown','BlindUp'],
 'appear': ['Appear','Fade']
},
toggle: function(element, effect, options) {
 element = $(element);
 effect  = (effect || 'appear').toLowerCase();
 
 return Effect[ Effect.PAIRS[ effect ][ element.visible() ? 1 : 0 ] ](element, Object.extend({
   queue: { position:'end', scope:(element.id || 'global'), limit: 1 }
 }, options || {}));
}
};

Effect.DefaultOptions.transition = Effect.Transitions.sinoidal;
Effect.ScopedQueue = Class.create(Enumerable, {
initialize: function() {
 this.effects  = [];
 this.interval = null;
},
_each: function(iterator) {
 this.effects._each(iterator);
},
add: function(effect) {
 var timestamp = new Date().getTime();

 var position = Object.isString(effect.options.queue) ?
   effect.options.queue : effect.options.queue.position;

 switch(position) {
   case 'front':
     // move unstarted effects after this effect
     this.effects.findAll(function(e){ return e.state=='idle' }).each( function(e) {
         e.startOn  += effect.finishOn;
         e.finishOn += effect.finishOn;
       });
     break;
   case 'with-last':
     timestamp = this.effects.pluck('startOn').max() || timestamp;
     break;
   case 'end':
     // start effect after last queued effect has finished
     timestamp = this.effects.pluck('finishOn').max() || timestamp;
     break;
 }

 effect.startOn  += timestamp;
 effect.finishOn += timestamp;

 if (!effect.options.queue.limit || (this.effects.length < effect.options.queue.limit))
   this.effects.push(effect);

 if (!this.interval)
   this.interval = setInterval(this.loop.bind(this), 15);
},
remove: function(effect) {
 this.effects = this.effects.reject(function(e) { return e==effect });
 if (this.effects.length == 0) {
   clearInterval(this.interval);
   this.interval = null;
 }
},
loop: function() {
 var timePos = new Date().getTime();
 for(var i=0, len=this.effects.length;i<len;i++)
   this.effects[i] && this.effects[i].loop(timePos);
}
});

Effect.Queues = {
instances: $H(),
get: function(queueName) {
 if (!Object.isString(queueName)) return queueName;

 return this.instances.get(queueName) ||
   this.instances.set(queueName, new Effect.ScopedQueue());
}
};
Effect.Queue = Effect.Queues.get('global');

Effect.Base = Class.create({
position: null,
start: function(options) {
 if (options && options.transition === false) options.transition = Effect.Transitions.linear;
 this.options      = Object.extend(Object.extend({ },Effect.DefaultOptions), options || { });
 this.currentFrame = 0;
 this.state        = 'idle';
 this.startOn      = this.options.delay*1000;
 this.finishOn     = this.startOn+(this.options.duration*1000);
 this.fromToDelta  = this.options.to-this.options.from;
 this.totalTime    = this.finishOn-this.startOn;
 this.totalFrames  = this.options.fps*this.options.duration;

 this.render = (function() {
   function dispatch(effect, eventName) {
     if (effect.options[eventName + 'Internal'])
       effect.options[eventName + 'Internal'](effect);
     if (effect.options[eventName])
       effect.options[eventName](effect);
   }

   return function(pos) {
     if (this.state === "idle") {
       this.state = "running";
       dispatch(this, 'beforeSetup');
       if (this.setup) this.setup();
       dispatch(this, 'afterSetup');
     }
     if (this.state === "running") {
       pos = (this.options.transition(pos) * this.fromToDelta) + this.options.from;
       this.position = pos;
       dispatch(this, 'beforeUpdate');
       if (this.update) this.update(pos);
       dispatch(this, 'afterUpdate');
     }
   };
 })();

 this.event('beforeStart');
 if (!this.options.sync)
   Effect.Queues.get(Object.isString(this.options.queue) ?
     'global' : this.options.queue.scope).add(this);
},
loop: function(timePos) {
 if (timePos >= this.startOn) {
   if (timePos >= this.finishOn) {
     this.render(1.0);
     this.cancel();
     this.event('beforeFinish');
     if (this.finish) this.finish();
     this.event('afterFinish');
     return;
   }
   var pos   = (timePos - this.startOn) / this.totalTime,
       frame = (pos * this.totalFrames).round();
   if (frame > this.currentFrame) {
     this.render(pos);
     this.currentFrame = frame;
   }
 }
},
cancel: function() {
 if (!this.options.sync)
   Effect.Queues.get(Object.isString(this.options.queue) ?
     'global' : this.options.queue.scope).remove(this);
 this.state = 'finished';
},
event: function(eventName) {
 if (this.options[eventName + 'Internal']) this.options[eventName + 'Internal'](this);
 if (this.options[eventName]) this.options[eventName](this);
},
inspect: function() {
 var data = $H();
 for(property in this)
   if (!Object.isFunction(this[property])) data.set(property, this[property]);
 return '#<Effect:' + data.inspect() + ',options:' + $H(this.options).inspect() + '>';
}
});

Effect.Parallel = Class.create(Effect.Base, {
initialize: function(effects) {
 this.effects = effects || [];
 this.start(arguments[1]);
},
update: function(position) {
 this.effects.invoke('render', position);
},
finish: function(position) {
 this.effects.each( function(effect) {
   effect.render(1.0);
   effect.cancel();
   effect.event('beforeFinish');
   if (effect.finish) effect.finish(position);
   effect.event('afterFinish');
 });
}
});

Effect.Tween = Class.create(Effect.Base, {
initialize: function(object, from, to) {
 object = Object.isString(object) ? $(object) : object;
 var args = $A(arguments), method = args.last(),
   options = args.length == 5 ? args[3] : null;
 this.method = Object.isFunction(method) ? method.bind(object) :
   Object.isFunction(object[method]) ? object[method].bind(object) :
   function(value) { object[method] = value; };
 this.start(Object.extend({ from: from, to: to }, options || { }));
},
update: function(position) {
 this.method(position);
}
});

Effect.Event = Class.create(Effect.Base, {
initialize: function() {
 this.start(Object.extend({ duration: 0 }, arguments[0] || { }));
},
update: Prototype.emptyFunction
});

Effect.Opacity = Class.create(Effect.Base, {
initialize: function(element) {
 this.element = $(element);
 if (!this.element) throw(Effect._elementDoesNotExistError);
 // make this work on IE on elements without 'layout'
 if (Prototype.Browser.IE && (!this.element.currentStyle.hasLayout))
   this.element.setStyle({zoom: 1});
 var options = Object.extend({
   from: this.element.getOpacity() || 0.0,
   to:   1.0
 }, arguments[1] || { });
 this.start(options);
},
update: function(position) {
 this.element.setOpacity(position);
}
});

Effect.Move = Class.create(Effect.Base, {
initialize: function(element) {
 this.element = $(element);
 if (!this.element) throw(Effect._elementDoesNotExistError);
 var options = Object.extend({
   x:    0,
   y:    0,
   mode: 'relative'
 }, arguments[1] || { });
 this.start(options);
},
setup: function() {
 this.element.makePositioned();
 this.originalLeft = parseFloat(this.element.getStyle('left') || '0');
 this.originalTop  = parseFloat(this.element.getStyle('top')  || '0');
 if (this.options.mode == 'absolute') {
   this.options.x = this.options.x - this.originalLeft;
   this.options.y = this.options.y - this.originalTop;
 }
},
update: function(position) {
 this.element.setStyle({
   left: (this.options.x  * position + this.originalLeft).round() + 'px',
   top:  (this.options.y  * position + this.originalTop).round()  + 'px'
 });
}
});

//for backwards compatibility
Effect.MoveBy = function(element, toTop, toLeft) {
return new Effect.Move(element,
 Object.extend({ x: toLeft, y: toTop }, arguments[3] || { }));
};

Effect.Scale = Class.create(Effect.Base, {
initialize: function(element, percent) {
 this.element = $(element);
 if (!this.element) throw(Effect._elementDoesNotExistError);
 var options = Object.extend({
   scaleX: true,
   scaleY: true,
   scaleContent: true,
   scaleFromCenter: false,
   scaleMode: 'box',        // 'box' or 'contents' or { } with provided values
   scaleFrom: 100.0,
   scaleTo:   percent
 }, arguments[2] || { });
 this.start(options);
},
setup: function() {
 this.restoreAfterFinish = this.options.restoreAfterFinish || false;
 this.elementPositioning = this.element.getStyle('position');

 this.originalStyle = { };
 ['top','left','width','height','fontSize'].each( function(k) {
   this.originalStyle[k] = this.element.style[k];
 }.bind(this));

 this.originalTop  = this.element.offsetTop;
 this.originalLeft = this.element.offsetLeft;

 var fontSize = this.element.getStyle('font-size') || '100%';
 ['em','px','%','pt'].each( function(fontSizeType) {
   if (fontSize.indexOf(fontSizeType)>0) {
     this.fontSize     = parseFloat(fontSize);
     this.fontSizeType = fontSizeType;
   }
 }.bind(this));

 this.factor = (this.options.scaleTo - this.options.scaleFrom)/100;

 this.dims = null;
 if (this.options.scaleMode=='box')
   this.dims = [this.element.offsetHeight, this.element.offsetWidth];
 if (/^content/.test(this.options.scaleMode))
   this.dims = [this.element.scrollHeight, this.element.scrollWidth];
 if (!this.dims)
   this.dims = [this.options.scaleMode.originalHeight,
                this.options.scaleMode.originalWidth];
},
update: function(position) {
 var currentScale = (this.options.scaleFrom/100.0) + (this.factor * position);
 if (this.options.scaleContent && this.fontSize)
   this.element.setStyle({fontSize: this.fontSize * currentScale + this.fontSizeType });
 this.setDimensions(this.dims[0] * currentScale, this.dims[1] * currentScale);
},
finish: function(position) {
 if (this.restoreAfterFinish) this.element.setStyle(this.originalStyle);
},
setDimensions: function(height, width) {
 var d = { };
 if (this.options.scaleX) d.width = width.round() + 'px';
 if (this.options.scaleY) d.height = height.round() + 'px';
 if (this.options.scaleFromCenter) {
   var topd  = (height - this.dims[0])/2;
   var leftd = (width  - this.dims[1])/2;
   if (this.elementPositioning == 'absolute') {
     if (this.options.scaleY) d.top = this.originalTop-topd + 'px';
     if (this.options.scaleX) d.left = this.originalLeft-leftd + 'px';
   } else {
     if (this.options.scaleY) d.top = -topd + 'px';
     if (this.options.scaleX) d.left = -leftd + 'px';
   }
 }
 this.element.setStyle(d);
}
});

Effect.Highlight = Class.create(Effect.Base, {
initialize: function(element) {
 this.element = $(element);
 if (!this.element) throw(Effect._elementDoesNotExistError);
 var options = Object.extend({ startcolor: '#ffff99' }, arguments[1] || { });
 this.start(options);
},
setup: function() {
 // Prevent executing on elements not in the layout flow
 if (this.element.getStyle('display')=='none') { this.cancel(); return; }
 // Disable background image during the effect
 this.oldStyle = { };
 if (!this.options.keepBackgroundImage) {
   this.oldStyle.backgroundImage = this.element.getStyle('background-image');
   this.element.setStyle({backgroundImage: 'none'});
 }
 if (!this.options.endcolor)
   this.options.endcolor = this.element.getStyle('background-color').parseColor('#ffffff');
 if (!this.options.restorecolor)
   this.options.restorecolor = this.element.getStyle('background-color');
 // init color calculations
 this._base  = $R(0,2).map(function(i){ return parseInt(this.options.startcolor.slice(i*2+1,i*2+3),16); }.bind(this));
 this._delta = $R(0,2).map(function(i){ return parseInt(this.options.endcolor.slice(i*2+1,i*2+3),16)-this._base[i]; }.bind(this));
},
update: function(position) {
 this.element.setStyle({backgroundColor: $R(0,2).inject('#',function(m,v,i){
   return m+((this._base[i]+(this._delta[i]*position)).round().toColorPart()); }.bind(this)) });
},
finish: function() {
 this.element.setStyle(Object.extend(this.oldStyle, {
   backgroundColor: this.options.restorecolor
 }));
}
});

Effect.ScrollTo = function(element) {
var options = arguments[1] || { },
scrollOffsets = document.viewport.getScrollOffsets(),
elementOffsets = $(element).cumulativeOffset();

if (options.offset) elementOffsets[1] += options.offset;

return new Effect.Tween(null,
 scrollOffsets.top,
 elementOffsets[1],
 options,
 function(p){ scrollTo(scrollOffsets.left, p.round()); }
);
};

Effect.Fade = function(element) {
element = $(element);
var oldOpacity = element.getInlineOpacity();
var options = Object.extend({
 from: element.getOpacity() || 1.0,
 to:   0.0,
 afterFinishInternal: function(effect) {
   if (effect.options.to!=0) return;
   effect.element.hide().setStyle({opacity: oldOpacity});
 }
}, arguments[1] || { });
return new Effect.Opacity(element,options);
};

Effect.Appear = function(element) {
element = $(element);
var options = Object.extend({
from: (element.getStyle('display') == 'none' ? 0.0 : element.getOpacity() || 0.0),
to:   1.0,
// force Safari to render floated elements properly
afterFinishInternal: function(effect) {
 effect.element.forceRerendering();
},
beforeSetup: function(effect) {
 effect.element.setOpacity(effect.options.from).show();
}}, arguments[1] || { });
return new Effect.Opacity(element,options);
};

Effect.Puff = function(element) {
element = $(element);
var oldStyle = {
 opacity: element.getInlineOpacity(),
 position: element.getStyle('position'),
 top:  element.style.top,
 left: element.style.left,
 width: element.style.width,
 height: element.style.height
};
return new Effect.Parallel(
[ new Effect.Scale(element, 200,
   { sync: true, scaleFromCenter: true, scaleContent: true, restoreAfterFinish: true }),
  new Effect.Opacity(element, { sync: true, to: 0.0 } ) ],
  Object.extend({ duration: 1.0,
   beforeSetupInternal: function(effect) {
     Position.absolutize(effect.effects[0].element);
   },
   afterFinishInternal: function(effect) {
      effect.effects[0].element.hide().setStyle(oldStyle); }
  }, arguments[1] || { })
);
};

Effect.BlindUp = function(element) {
element = $(element);
element.makeClipping();
return new Effect.Scale(element, 0,
 Object.extend({ scaleContent: false,
   scaleX: false,
   restoreAfterFinish: true,
   afterFinishInternal: function(effect) {
     effect.element.hide().undoClipping();
   }
 }, arguments[1] || { })
);
};

Effect.BlindDown = function(element) {
element = $(element);
var elementDimensions = element.getDimensions();
return new Effect.Scale(element, 100, Object.extend({
 scaleContent: false,
 scaleX: false,
 scaleFrom: 0,
 scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
 restoreAfterFinish: true,
 afterSetup: function(effect) {
   effect.element.makeClipping().setStyle({height: '0px'}).show();
 },
 afterFinishInternal: function(effect) {
   effect.element.undoClipping();
 }
}, arguments[1] || { }));
};

Effect.SwitchOff = function(element) {
element = $(element);
var oldOpacity = element.getInlineOpacity();
return new Effect.Appear(element, Object.extend({
 duration: 0.4,
 from: 0,
 transition: Effect.Transitions.flicker,
 afterFinishInternal: function(effect) {
   new Effect.Scale(effect.element, 1, {
     duration: 0.3, scaleFromCenter: true,
     scaleX: false, scaleContent: false, restoreAfterFinish: true,
     beforeSetup: function(effect) {
       effect.element.makePositioned().makeClipping();
     },
     afterFinishInternal: function(effect) {
       effect.element.hide().undoClipping().undoPositioned().setStyle({opacity: oldOpacity});
     }
   });
 }
}, arguments[1] || { }));
};

Effect.DropOut = function(element) {
element = $(element);
var oldStyle = {
 top: element.getStyle('top'),
 left: element.getStyle('left'),
 opacity: element.getInlineOpacity() };
return new Effect.Parallel(
 [ new Effect.Move(element, {x: 0, y: 100, sync: true }),
   new Effect.Opacity(element, { sync: true, to: 0.0 }) ],
 Object.extend(
   { duration: 0.5,
     beforeSetup: function(effect) {
       effect.effects[0].element.makePositioned();
     },
     afterFinishInternal: function(effect) {
       effect.effects[0].element.hide().undoPositioned().setStyle(oldStyle);
     }
   }, arguments[1] || { }));
};

Effect.Shake = function(element) {
element = $(element);
var options = Object.extend({
 distance: 20,
 duration: 0.5
}, arguments[1] || {});
var distance = parseFloat(options.distance);
var split = parseFloat(options.duration) / 10.0;
var oldStyle = {
 top: element.getStyle('top'),
 left: element.getStyle('left') };
 return new Effect.Move(element,
   { x:  distance, y: 0, duration: split, afterFinishInternal: function(effect) {
 new Effect.Move(effect.element,
   { x: -distance*2, y: 0, duration: split*2,  afterFinishInternal: function(effect) {
 new Effect.Move(effect.element,
   { x:  distance*2, y: 0, duration: split*2,  afterFinishInternal: function(effect) {
 new Effect.Move(effect.element,
   { x: -distance*2, y: 0, duration: split*2,  afterFinishInternal: function(effect) {
 new Effect.Move(effect.element,
   { x:  distance*2, y: 0, duration: split*2,  afterFinishInternal: function(effect) {
 new Effect.Move(effect.element,
   { x: -distance, y: 0, duration: split, afterFinishInternal: function(effect) {
     effect.element.undoPositioned().setStyle(oldStyle);
}}); }}); }}); }}); }}); }});
};

Effect.SlideDown = function(element) {
element = $(element).cleanWhitespace();
// SlideDown need to have the content of the element wrapped in a container element with fixed height!
var oldInnerBottom = element.down().getStyle('bottom');
var elementDimensions = element.getDimensions();
return new Effect.Scale(element, 100, Object.extend({
 scaleContent: false,
 scaleX: false,
 scaleFrom: window.opera ? 0 : 1,
 scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
 restoreAfterFinish: true,
 afterSetup: function(effect) {
   effect.element.makePositioned();
   effect.element.down().makePositioned();
   if (window.opera) effect.element.setStyle({top: ''});
   effect.element.makeClipping().setStyle({height: '0px'}).show();
 },
 afterUpdateInternal: function(effect) {
   effect.element.down().setStyle({bottom:
     (effect.dims[0] - effect.element.clientHeight) + 'px' });
 },
 afterFinishInternal: function(effect) {
   effect.element.undoClipping().undoPositioned();
   effect.element.down().undoPositioned().setStyle({bottom: oldInnerBottom}); }
 }, arguments[1] || { })
);
};

Effect.SlideUp = function(element) {
element = $(element).cleanWhitespace();
var oldInnerBottom = element.down().getStyle('bottom');
var elementDimensions = element.getDimensions();
return new Effect.Scale(element, window.opera ? 0 : 1,
Object.extend({ scaleContent: false,
 scaleX: false,
 scaleMode: 'box',
 scaleFrom: 100,
 scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
 restoreAfterFinish: true,
 afterSetup: function(effect) {
   effect.element.makePositioned();
   effect.element.down().makePositioned();
   if (window.opera) effect.element.setStyle({top: ''});
   effect.element.makeClipping().show();
 },
 afterUpdateInternal: function(effect) {
   effect.element.down().setStyle({bottom:
     (effect.dims[0] - effect.element.clientHeight) + 'px' });
 },
 afterFinishInternal: function(effect) {
   effect.element.hide().undoClipping().undoPositioned();
   effect.element.down().undoPositioned().setStyle({bottom: oldInnerBottom});
 }
}, arguments[1] || { })
);
};

//Bug in opera makes the TD containing this element expand for a instance after finish
Effect.Squish = function(element) {
return new Effect.Scale(element, window.opera ? 1 : 0, {
 restoreAfterFinish: true,
 beforeSetup: function(effect) {
   effect.element.makeClipping();
 },
 afterFinishInternal: function(effect) {
   effect.element.hide().undoClipping();
 }
});
};

Effect.Grow = function(element) {
element = $(element);
var options = Object.extend({
 direction: 'center',
 moveTransition: Effect.Transitions.sinoidal,
 scaleTransition: Effect.Transitions.sinoidal,
 opacityTransition: Effect.Transitions.full
}, arguments[1] || { });
var oldStyle = {
 top: element.style.top,
 left: element.style.left,
 height: element.style.height,
 width: element.style.width,
 opacity: element.getInlineOpacity() };

var dims = element.getDimensions();
var initialMoveX, initialMoveY;
var moveX=null, moveY=null;

switch (options.direction) {
 case 'top-left':
   initialMoveX = initialMoveY = moveX = moveY = 0;
   break;
 case 'top-right':
   initialMoveX = dims.width;
   initialMoveY = moveY = 0;
   moveX = -dims.width;
   break;
 case 'bottom-left':
   initialMoveX = moveX = 0;
   initialMoveY = dims.height;
   moveY = -dims.height;
   break;
 case 'bottom-right':
   initialMoveX = dims.width;
   initialMoveY = dims.height;
   moveX = -dims.width;
   moveY = -dims.height;
   break;
 case 'center':
   initialMoveX = dims.width / 2;
   initialMoveY = dims.height / 2;
   moveX = -dims.width / 2;
   moveY = -dims.height / 2;
   break;
}

return new Effect.Move(element, {
 x: initialMoveX,
 y: initialMoveY,
 duration: 0.01,
 beforeSetup: function(effect) {
   effect.element.hide().makeClipping().makePositioned();
 },
 afterFinishInternal: function(effect) {
   new Effect.Parallel(
     [ new Effect.Opacity(effect.element, { sync: true, to: 1.0, from: 0.0, transition: options.opacityTransition }),
       new Effect.Move(effect.element, { x: moveX, y: moveY, sync: true, transition: options.moveTransition }),
       new Effect.Scale(effect.element, 100, {
         scaleMode: { originalHeight: dims.height, originalWidth: dims.width },
         sync: true, scaleFrom: window.opera ? 1 : 0, transition: options.scaleTransition, restoreAfterFinish: true})
     ], Object.extend({
          beforeSetup: function(effect) {
            effect.effects[0].element.setStyle({height: '0px'}).show();
          },
          afterFinishInternal: function(effect) {
            effect.effects[0].element.undoClipping().undoPositioned().setStyle(oldStyle);
          }
        }, options)
   );
 }
});
};

Effect.Shrink = function(element) {
element = $(element);
var options = Object.extend({
 direction: 'center',
 moveTransition: Effect.Transitions.sinoidal,
 scaleTransition: Effect.Transitions.sinoidal,
 opacityTransition: Effect.Transitions.none
}, arguments[1] || { });
var oldStyle = {
 top: element.style.top,
 left: element.style.left,
 height: element.style.height,
 width: element.style.width,
 opacity: element.getInlineOpacity() };

var dims = element.getDimensions();
var moveX, moveY;

switch (options.direction) {
 case 'top-left':
   moveX = moveY = 0;
   break;
 case 'top-right':
   moveX = dims.width;
   moveY = 0;
   break;
 case 'bottom-left':
   moveX = 0;
   moveY = dims.height;
   break;
 case 'bottom-right':
   moveX = dims.width;
   moveY = dims.height;
   break;
 case 'center':
   moveX = dims.width / 2;
   moveY = dims.height / 2;
   break;
}

return new Effect.Parallel(
 [ new Effect.Opacity(element, { sync: true, to: 0.0, from: 1.0, transition: options.opacityTransition }),
   new Effect.Scale(element, window.opera ? 1 : 0, { sync: true, transition: options.scaleTransition, restoreAfterFinish: true}),
   new Effect.Move(element, { x: moveX, y: moveY, sync: true, transition: options.moveTransition })
 ], Object.extend({
      beforeStartInternal: function(effect) {
        effect.effects[0].element.makePositioned().makeClipping();
      },
      afterFinishInternal: function(effect) {
        effect.effects[0].element.hide().undoClipping().undoPositioned().setStyle(oldStyle); }
    }, options)
);
};

Effect.Pulsate = function(element) {
element = $(element);
var options    = arguments[1] || { },
 oldOpacity = element.getInlineOpacity(),
 transition = options.transition || Effect.Transitions.linear,
 reverser   = function(pos){
   return 1 - transition((-Math.cos((pos*(options.pulses||5)*2)*Math.PI)/2) + .5);
 };

return new Effect.Opacity(element,
 Object.extend(Object.extend({  duration: 2.0, from: 0,
   afterFinishInternal: function(effect) { effect.element.setStyle({opacity: oldOpacity}); }
 }, options), {transition: reverser}));
};

Effect.Fold = function(element) {
element = $(element);
var oldStyle = {
 top: element.style.top,
 left: element.style.left,
 width: element.style.width,
 height: element.style.height };
element.makeClipping();
return new Effect.Scale(element, 5, Object.extend({
 scaleContent: false,
 scaleX: false,
 afterFinishInternal: function(effect) {
 new Effect.Scale(element, 1, {
   scaleContent: false,
   scaleY: false,
   afterFinishInternal: function(effect) {
     effect.element.hide().undoClipping().setStyle(oldStyle);
   } });
}}, arguments[1] || { }));
};

Effect.Morph = Class.create(Effect.Base, {
initialize: function(element) {
 this.element = $(element);
 if (!this.element) throw(Effect._elementDoesNotExistError);
 var options = Object.extend({
   style: { }
 }, arguments[1] || { });

 if (!Object.isString(options.style)) this.style = $H(options.style);
 else {
   if (options.style.include(':'))
     this.style = options.style.parseStyle();
   else {
     this.element.addClassName(options.style);
     this.style = $H(this.element.getStyles());
     this.element.removeClassName(options.style);
     var css = this.element.getStyles();
     this.style = this.style.reject(function(style) {
       return style.value == css[style.key];
     });
     options.afterFinishInternal = function(effect) {
       effect.element.addClassName(effect.options.style);
       effect.transforms.each(function(transform) {
         effect.element.style[transform.style] = '';
       });
     };
   }
 }
 this.start(options);
},

setup: function(){
 function parseColor(color){
   if (!color || ['rgba(0, 0, 0, 0)','transparent'].include(color)) color = '#ffffff';
   color = color.parseColor();
   return $R(0,2).map(function(i){
     return parseInt( color.slice(i*2+1,i*2+3), 16 );
   });
 }
 this.transforms = this.style.map(function(pair){
   var property = pair[0], value = pair[1], unit = null;

   if (value.parseColor('#zzzzzz') != '#zzzzzz') {
     value = value.parseColor();
     unit  = 'color';
   } else if (property == 'opacity') {
     value = parseFloat(value);
     if (Prototype.Browser.IE && (!this.element.currentStyle.hasLayout))
       this.element.setStyle({zoom: 1});
   } else if (Element.CSS_LENGTH.test(value)) {
       var components = value.match(/^([\+\-]?[0-9\.]+)(.*)$/);
       value = parseFloat(components[1]);
       unit = (components.length == 3) ? components[2] : null;
   }

   var originalValue = this.element.getStyle(property);
   return {
     style: property.camelize(),
     originalValue: unit=='color' ? parseColor(originalValue) : parseFloat(originalValue || 0),
     targetValue: unit=='color' ? parseColor(value) : value,
     unit: unit
   };
 }.bind(this)).reject(function(transform){
   return (
     (transform.originalValue == transform.targetValue) ||
     (
       transform.unit != 'color' &&
       (isNaN(transform.originalValue) || isNaN(transform.targetValue))
     )
   );
 });
},
update: function(position) {
 var style = { }, transform, i = this.transforms.length;
 while(i--)
   style[(transform = this.transforms[i]).style] =
     transform.unit=='color' ? '#'+
       (Math.round(transform.originalValue[0]+
         (transform.targetValue[0]-transform.originalValue[0])*position)).toColorPart() +
       (Math.round(transform.originalValue[1]+
         (transform.targetValue[1]-transform.originalValue[1])*position)).toColorPart() +
       (Math.round(transform.originalValue[2]+
         (transform.targetValue[2]-transform.originalValue[2])*position)).toColorPart() :
     (transform.originalValue +
       (transform.targetValue - transform.originalValue) * position).toFixed(3) +
         (transform.unit === null ? '' : transform.unit);
 this.element.setStyle(style, true);
}
});

Effect.Transform = Class.create({
initialize: function(tracks){
 this.tracks  = [];
 this.options = arguments[1] || { };
 this.addTracks(tracks);
},
addTracks: function(tracks){
 tracks.each(function(track){
   track = $H(track);
   var data = track.values().first();
   this.tracks.push($H({
     ids:     track.keys().first(),
     effect:  Effect.Morph,
     options: { style: data }
   }));
 }.bind(this));
 return this;
},
play: function(){
 return new Effect.Parallel(
   this.tracks.map(function(track){
     var ids = track.get('ids'), effect = track.get('effect'), options = track.get('options');
     var elements = [$(ids) || $$(ids)].flatten();
     return elements.map(function(e){ return new effect(e, Object.extend({ sync:true }, options)) });
   }).flatten(),
   this.options
 );
}
});

Element.CSS_PROPERTIES = $w(
'backgroundColor backgroundPosition borderBottomColor borderBottomStyle ' +
'borderBottomWidth borderLeftColor borderLeftStyle borderLeftWidth ' +
'borderRightColor borderRightStyle borderRightWidth borderSpacing ' +
'borderTopColor borderTopStyle borderTopWidth bottom clip color ' +
'fontSize fontWeight height left letterSpacing lineHeight ' +
'marginBottom marginLeft marginRight marginTop markerOffset maxHeight '+
'maxWidth minHeight minWidth opacity outlineColor outlineOffset ' +
'outlineWidth paddingBottom paddingLeft paddingRight paddingTop ' +
'right textIndent top width wordSpacing zIndex');

Element.CSS_LENGTH = /^(([\+\-]?[0-9\.]+)(em|ex|px|in|cm|mm|pt|pc|\%))|0$/;

String.__parseStyleElement = document.createElement('div');
String.prototype.parseStyle = function(){
var style, styleRules = $H();
if (Prototype.Browser.WebKit)
 style = new Element('div',{style:this}).style;
else {
 String.__parseStyleElement.innerHTML = '<div style="' + this + '"></div>';
 style = String.__parseStyleElement.childNodes[0].style;
}

Element.CSS_PROPERTIES.each(function(property){
 if (style[property]) styleRules.set(property, style[property]);
});

if (Prototype.Browser.IE && this.include('opacity'))
 styleRules.set('opacity', this.match(/opacity:\s*((?:0|1)?(?:\.\d*)?)/)[1]);

return styleRules;
};

if (document.defaultView && document.defaultView.getComputedStyle) {
Element.getStyles = function(element) {
 var css = document.defaultView.getComputedStyle($(element), null);
 return Element.CSS_PROPERTIES.inject({ }, function(styles, property) {
   styles[property] = css[property];
   return styles;
 });
};
} else {
Element.getStyles = function(element) {
 element = $(element);
 var css = element.currentStyle, styles;
 styles = Element.CSS_PROPERTIES.inject({ }, function(results, property) {
   results[property] = css[property];
   return results;
 });
 if (!styles.opacity) styles.opacity = element.getOpacity();
 return styles;
};
}

Effect.Methods = {
morph: function(element, style) {
 element = $(element);
 new Effect.Morph(element, Object.extend({ style: style }, arguments[2] || { }));
 return element;
},
visualEffect: function(element, effect, options) {
 element = $(element);
 var s = effect.dasherize().camelize(), klass = s.charAt(0).toUpperCase() + s.substring(1);
 new Effect[klass](element, options);
 return element;
},
highlight: function(element, options) {
 element = $(element);
 new Effect.Highlight(element, options);
 return element;
}
};

$w('fade appear grow shrink fold blindUp blindDown slideUp slideDown '+
'pulsate shake puff squish switchOff dropOut').each(
function(effect) {
 Effect.Methods[effect] = function(element, options){
   element = $(element);
   Effect[effect.charAt(0).toUpperCase() + effect.substring(1)](element, options);
   return element;
 };
}
);

$w('getInlineOpacity forceRerendering setContentZoom collectTextNodes collectTextNodesIgnoreClass getStyles').each(
function(f) { Effect.Methods[f] = Element[f]; }
);

Element.addMethods(Effect.Methods);
//------------------------------------------------------------------------------------------------//
// Script:builder.js
//------------------------------------------------------------------------------------------------//
// script.aculo.us builder.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

// Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/
var Builder = {
  NODEMAP: {
    AREA: 'map',
    CAPTION: 'table',
    COL: 'table',
    COLGROUP: 'table',
    LEGEND: 'fieldset',
    OPTGROUP: 'select',
    OPTION: 'select',
    PARAM: 'object',
    TBODY: 'table',
    TD: 'table',
    TFOOT: 'table',
    TH: 'table',
    THEAD: 'table',
    TR: 'table'
  },
  // note: For Firefox < 1.5, OPTION and OPTGROUP tags are currently broken,
  //       due to a Firefox bug
  node: function(elementName) {
    elementName = elementName.toUpperCase();

    // try innerHTML approach
    var parentTag = this.NODEMAP[elementName] || 'div';
    var parentElement = document.createElement(parentTag);
    try { // prevent IE "feature": http://dev.rubyonrails.org/ticket/2707
      parentElement.innerHTML = "<" + elementName + "></" + elementName + ">";
    } catch(e) {}
    var element = parentElement.firstChild || null;

    // see if browser added wrapping tags
    if(element && (element.tagName.toUpperCase() != elementName))
      element = element.getElementsByTagName(elementName)[0];

    // fallback to createElement approach
    if(!element) element = document.createElement(elementName);

    // abort if nothing could be created
    if(!element) return;

    // attributes (or text)
    if(arguments[1])
      if(this._isStringOrNumber(arguments[1]) ||
        (arguments[1] instanceof Array) ||
        arguments[1].tagName) {
          this._children(element, arguments[1]);
        } else {
          var attrs = this._attributes(arguments[1]);
          if(attrs.length) {
            try { // prevent IE "feature": http://dev.rubyonrails.org/ticket/2707
              parentElement.innerHTML = "<" +elementName + " " +
                attrs + "></" + elementName + ">";
            } catch(e) {}
            element = parentElement.firstChild || null;
            // workaround firefox 1.0.X bug
            if(!element) {
              element = document.createElement(elementName);
              for(attr in arguments[1])
                element[attr == 'class' ? 'className' : attr] = arguments[1][attr];
            }
            if(element.tagName.toUpperCase() != elementName)
              element = parentElement.getElementsByTagName(elementName)[0];
          }
        }

    // text, or array of children
    if(arguments[2])
      this._children(element, arguments[2]);

     return $(element);
  },
  _text: function(text) {
     return document.createTextNode(text);
  },

  ATTR_MAP: {
    'className': 'class',
    'htmlFor': 'for'
  },

  _attributes: function(attributes) {
    var attrs = [];
    for(attribute in attributes)
      attrs.push((attribute in this.ATTR_MAP ? this.ATTR_MAP[attribute] : attribute) +
          '="' + attributes[attribute].toString().escapeHTML().gsub(/"/,'&quot;') + '"');
    return attrs.join(" ");
  },
  _children: function(element, children) {
    if(children.tagName) {
      element.appendChild(children);
      return;
    }
    if(typeof children=='object') { // array can hold nodes and text
      children.flatten().each( function(e) {
        if(typeof e=='object')
          element.appendChild(e);
        else
          if(Builder._isStringOrNumber(e))
            element.appendChild(Builder._text(e));
      });
    } else
      if(Builder._isStringOrNumber(children))
        element.appendChild(Builder._text(children));
  },
  _isStringOrNumber: function(param) {
    return(typeof param=='string' || typeof param=='number');
  },
  build: function(html) {
    var element = this.node('div');
    $(element).update(html.strip());
    return element.down();
  },
  dump: function(scope) {
    if(typeof scope != 'object' && typeof scope != 'function') scope = window; //global scope

    var tags = ("A ABBR ACRONYM ADDRESS APPLET AREA B BASE BASEFONT BDO BIG BLOCKQUOTE BODY " +
      "BR BUTTON CAPTION CENTER CITE CODE COL COLGROUP DD DEL DFN DIR DIV DL DT EM FIELDSET " +
      "FONT FORM FRAME FRAMESET H1 H2 H3 H4 H5 H6 HEAD HR HTML I IFRAME IMG INPUT INS ISINDEX "+
      "KBD LABEL LEGEND LI LINK MAP MENU META NOFRAMES NOSCRIPT OBJECT OL OPTGROUP OPTION P "+
      "PARAM PRE Q S SAMP SCRIPT SELECT SMALL SPAN STRIKE STRONG STYLE SUB SUP TABLE TBODY TD "+
      "TEXTAREA TFOOT TH THEAD TITLE TR TT U UL VAR").split(/\s+/);

    tags.each( function(tag){
      scope[tag] = function() {
        return Builder.node.apply(Builder, [tag].concat($A(arguments)));
      };
    });
  }
};
//------------------------------------------------------------------------------------------------//
// Script:controls.js
//------------------------------------------------------------------------------------------------//
//script.aculo.us controls.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

//Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//        (c) 2005-2009 Ivan Krstic (http://blogs.law.harvard.edu/ivan)
//        (c) 2005-2009 Jon Tirsen (http://www.tirsen.com)
//Contributors:
//Richard Livsey
//Rahul Bhargava
//Rob Wills
//
//script.aculo.us is freely distributable under the terms of an MIT-style license.
//For details, see the script.aculo.us web site: http://script.aculo.us/

//Autocompleter.Base handles all the autocompletion functionality
//that's independent of the data source for autocompletion. This
//includes drawing the autocompletion menu, observing keyboard
//and mouse events, and similar.
//
//Specific autocompleters need to provide, at the very least,
//a getUpdatedChoices function that will be invoked every time
//the text inside the monitored textbox changes. This method
//should get the text for which to provide autocompletion by
//invoking this.getToken(), NOT by directly accessing
//this.element.value. This is to allow incremental tokenized
//autocompletion. Specific auto-completion logic (AJAX, etc)
//belongs in getUpdatedChoices.
//
//Tokenized incremental autocompletion is enabled automatically
//when an autocompleter is instantiated with the 'tokens' option
//in the options parameter, e.g.:
//new Ajax.Autocompleter('id','upd', '/url/', { tokens: ',' });
//will incrementally autocomplete with a comma as the token.
//Additionally, ',' in the above example can be replaced with
//a token array, e.g. { tokens: [',', '\n'] } which
//enables autocompletion on multiple tokens. This is most
//useful when one of the tokens is \n (a newline), as it
//allows smart autocompletion after linebreaks.

if(typeof Effect == 'undefined')
throw("controls.js requires including script.aculo.us' effects.js library");

var Autocompleter = { };
Autocompleter.Base = Class.create({
baseInitialize: function(element, update, options) {
 element          = $(element);
 this.element     = element;
 this.update      = $(update);
 this.hasFocus    = false;
 this.changed     = false;
 this.active      = false;
 this.index       = 0;
 this.entryCount  = 0;
 this.oldElementValue = this.element.value;

 if(this.setOptions)
   this.setOptions(options);
 else
   this.options = options || { };

 this.options.paramName    = this.options.paramName || this.element.name;
 this.options.tokens       = this.options.tokens || [];
 this.options.frequency    = this.options.frequency || 0.4;
 this.options.minChars     = this.options.minChars || 1;
 this.options.onShow       = this.options.onShow ||
   function(element, update){
     if(!update.style.position || update.style.position=='absolute') {
       update.style.position = 'absolute';
       Position.clone(element, update, {
         setHeight: false,
         offsetTop: element.offsetHeight
       });
     }
     Effect.Appear(update,{duration:0.15});
   };
 this.options.onHide = this.options.onHide ||
   function(element, update){ new Effect.Fade(update,{duration:0.15}) };

 if(typeof(this.options.tokens) == 'string')
   this.options.tokens = new Array(this.options.tokens);
 // Force carriage returns as token delimiters anyway
 if (!this.options.tokens.include('\n'))
   this.options.tokens.push('\n');

 this.observer = null;

 this.element.setAttribute('autocomplete','off');

 Element.hide(this.update);

 Event.observe(this.element, 'blur', this.onBlur.bindAsEventListener(this));
 Event.observe(this.element, 'keydown', this.onKeyPress.bindAsEventListener(this));
},

show: function() {
 if(Element.getStyle(this.update, 'display')=='none') this.options.onShow(this.element, this.update);
 if(!this.iefix &&
   (Prototype.Browser.IE) &&
   (Element.getStyle(this.update, 'position')=='absolute')) {
   new Insertion.After(this.update,
    '<iframe id="' + this.update.id + '_iefix" '+
    'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
    'src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
   this.iefix = $(this.update.id+'_iefix');
 }
 if(this.iefix) setTimeout(this.fixIEOverlapping.bind(this), 50);
},

fixIEOverlapping: function() {
 Position.clone(this.update, this.iefix, {setTop:(!this.update.style.height)});
 this.iefix.style.zIndex = 1;
 this.update.style.zIndex = 2;
 Element.show(this.iefix);
},

hide: function() {
 this.stopIndicator();
 if(Element.getStyle(this.update, 'display')!='none') this.options.onHide(this.element, this.update);
 if(this.iefix) Element.hide(this.iefix);
},

startIndicator: function() {
 if(this.options.indicator) Element.show(this.options.indicator);
},

stopIndicator: function() {
 if(this.options.indicator) Element.hide(this.options.indicator);
},

onKeyPress: function(event) {
 if(this.active)
   switch(event.keyCode) {
    case Event.KEY_TAB:
    case Event.KEY_RETURN:
      this.selectEntry();
      Event.stop(event);
    case Event.KEY_ESC:
      this.hide();
      this.active = false;
      Event.stop(event);
      return;
    case Event.KEY_LEFT:
    case Event.KEY_RIGHT:
      return;
    case Event.KEY_UP:
      this.markPrevious();
      this.render();
      Event.stop(event);
      return;
    case Event.KEY_DOWN:
      this.markNext();
      this.render();
      Event.stop(event);
      return;
   }
  else
    if(event.keyCode==Event.KEY_TAB || event.keyCode==Event.KEY_RETURN ||
      (Prototype.Browser.WebKit > 0 && event.keyCode == 0)) return;

 this.changed = true;
 this.hasFocus = true;

 if(this.observer) clearTimeout(this.observer);
   this.observer =
     setTimeout(this.onObserverEvent.bind(this), this.options.frequency*1000);
},

activate: function() {
 this.changed = false;
 this.hasFocus = true;
 this.getUpdatedChoices();
},

onHover: function(event) {
 var element = Event.findElement(event, 'LI');
 if(this.index != element.autocompleteIndex)
 {
     this.index = element.autocompleteIndex;
     this.render();
 }
 Event.stop(event);
},

onClick: function(event) {
 var element = Event.findElement(event, 'LI');
 this.index = element.autocompleteIndex;
 this.selectEntry();
 this.hide();
},

onBlur: function(event) {
 // needed to make click events working
 setTimeout(this.hide.bind(this), 250);
 this.hasFocus = false;
 this.active = false;
},

render: function() {
 if(this.entryCount > 0) {
   for (var i = 0; i < this.entryCount; i++)
     this.index==i ?
       Element.addClassName(this.getEntry(i),"selected") :
       Element.removeClassName(this.getEntry(i),"selected");
   if(this.hasFocus) {
     this.show();
     this.active = true;
   }
 } else {
   this.active = false;
   this.hide();
 }
},

markPrevious: function() {
 if(this.index > 0) this.index--;
   else this.index = this.entryCount-1;
 this.getEntry(this.index).scrollIntoView(true);
},

markNext: function() {
 if(this.index < this.entryCount-1) this.index++;
   else this.index = 0;
 this.getEntry(this.index).scrollIntoView(false);
},

getEntry: function(index) {
 return this.update.firstChild.childNodes[index];
},

getCurrentEntry: function() {
 return this.getEntry(this.index);
},

selectEntry: function() {
 this.active = false;
 this.updateElement(this.getCurrentEntry());
},

updateElement: function(selectedElement) {
 if (this.options.updateElement) {
   this.options.updateElement(selectedElement);
   return;
 }
 var value = '';
 if (this.options.select) {
   var nodes = $(selectedElement).select('.' + this.options.select) || [];
   if(nodes.length>0) value = Element.collectTextNodes(nodes[0], this.options.select);
 } else
   value = Element.collectTextNodesIgnoreClass(selectedElement, 'informal');

 var bounds = this.getTokenBounds();
 if (bounds[0] != -1) {
   var newValue = this.element.value.substr(0, bounds[0]);
   var whitespace = this.element.value.substr(bounds[0]).match(/^\s+/);
   if (whitespace)
     newValue += whitespace[0];
   this.element.value = newValue + value + this.element.value.substr(bounds[1]);
 } else {
   this.element.value = value;
 }
 this.oldElementValue = this.element.value;
 this.element.focus();

 if (this.options.afterUpdateElement)
   this.options.afterUpdateElement(this.element, selectedElement);
},

updateChoices: function(choices) {
 if(!this.changed && this.hasFocus) {
   this.update.innerHTML = choices;
   Element.cleanWhitespace(this.update);
   Element.cleanWhitespace(this.update.down());

   if(this.update.firstChild && this.update.down().childNodes) {
     this.entryCount =
       this.update.down().childNodes.length;
     for (var i = 0; i < this.entryCount; i++) {
       var entry = this.getEntry(i);
       entry.autocompleteIndex = i;
       this.addObservers(entry);
     }
   } else {
     this.entryCount = 0;
   }

   this.stopIndicator();
   this.index = 0;

   if(this.entryCount==1 && this.options.autoSelect) {
     this.selectEntry();
     this.hide();
   } else {
     this.render();
   }
 }
},

addObservers: function(element) {
 Event.observe(element, "mouseover", this.onHover.bindAsEventListener(this));
 Event.observe(element, "click", this.onClick.bindAsEventListener(this));
},

onObserverEvent: function() {
 this.changed = false;
 this.tokenBounds = null;
 if(this.getToken().length>=this.options.minChars) {
   this.getUpdatedChoices();
 } else {
   this.active = false;
   this.hide();
 }
 this.oldElementValue = this.element.value;
},

getToken: function() {
 var bounds = this.getTokenBounds();
 return this.element.value.substring(bounds[0], bounds[1]).strip();
},

getTokenBounds: function() {
 if (null != this.tokenBounds) return this.tokenBounds;
 var value = this.element.value;
 if (value.strip().empty()) return [-1, 0];
 var diff = arguments.callee.getFirstDifferencePos(value, this.oldElementValue);
 var offset = (diff == this.oldElementValue.length ? 1 : 0);
 var prevTokenPos = -1, nextTokenPos = value.length;
 var tp;
 for (var index = 0, l = this.options.tokens.length; index < l; ++index) {
   tp = value.lastIndexOf(this.options.tokens[index], diff + offset - 1);
   if (tp > prevTokenPos) prevTokenPos = tp;
   tp = value.indexOf(this.options.tokens[index], diff + offset);
   if (-1 != tp && tp < nextTokenPos) nextTokenPos = tp;
 }
 return (this.tokenBounds = [prevTokenPos + 1, nextTokenPos]);
}
});

Autocompleter.Base.prototype.getTokenBounds.getFirstDifferencePos = function(newS, oldS) {
var boundary = Math.min(newS.length, oldS.length);
for (var index = 0; index < boundary; ++index)
 if (newS[index] != oldS[index])
   return index;
return boundary;
};

Ajax.Autocompleter = Class.create(Autocompleter.Base, {
initialize: function(element, update, url, options) {
 this.baseInitialize(element, update, options);
 this.options.asynchronous  = true;
 this.options.onComplete    = this.onComplete.bind(this);
 this.options.defaultParams = this.options.parameters || null;
 this.url                   = url;
},

getUpdatedChoices: function() {
 this.startIndicator();

 var entry = encodeURIComponent(this.options.paramName) + '=' +
   encodeURIComponent(this.getToken());

 this.options.parameters = this.options.callback ?
   this.options.callback(this.element, entry) : entry;

 if(this.options.defaultParams)
   this.options.parameters += '&' + this.options.defaultParams;

 new Ajax.Request(this.url, this.options);
},

onComplete: function(request) {
 this.updateChoices(request.responseText);
}
});

//The local array autocompleter. Used when you'd prefer to
//inject an array of autocompletion options into the page, rather
//than sending out Ajax queries, which can be quite slow sometimes.
//
//The constructor takes four parameters. The first two are, as usual,
//the id of the monitored textbox, and id of the autocompletion menu.
//The third is the array you want to autocomplete from, and the fourth
//is the options block.
//
//Extra local autocompletion options:
//- choices - How many autocompletion choices to offer
//
//- partialSearch - If false, the autocompleter will match entered
//                 text only at the beginning of strings in the
//                 autocomplete array. Defaults to true, which will
//                 match text at the beginning of any *word* in the
//                 strings in the autocomplete array. If you want to
//                 search anywhere in the string, additionally set
//                 the option fullSearch to true (default: off).
//
//- fullSsearch - Search anywhere in autocomplete array strings.
//
//- partialChars - How many characters to enter before triggering
//                a partial match (unlike minChars, which defines
//                how many characters are required to do any match
//                at all). Defaults to 2.
//
//- ignoreCase - Whether to ignore case when autocompleting.
//              Defaults to true.
//
//It's possible to pass in a custom function as the 'selector'
//option, if you prefer to write your own autocompletion logic.
//In that case, the other options above will not apply unless
//you support them.

Autocompleter.Local = Class.create(Autocompleter.Base, {
initialize: function(element, update, array, options) {
 this.baseInitialize(element, update, options);
 this.options.array = array;
},

getUpdatedChoices: function() {
 this.updateChoices(this.options.selector(this));
},

setOptions: function(options) {
 this.options = Object.extend({
   choices: 10,
   partialSearch: true,
   partialChars: 2,
   ignoreCase: true,
   fullSearch: false,
   selector: function(instance) {
     var ret       = new Array(); // WARNING
     var partial   = new Array(); // WARNING
     var entry     = instance.getToken();
     var count     = 0;

     for (var i = 0; i < instance.options.array.length &&
       ret.length < instance.options.choices ; i++) {

       var elem = instance.options.array[i];
       var foundPos = instance.options.ignoreCase ?
         elem.toLowerCase().indexOf(entry.toLowerCase()) :
         elem.indexOf(entry);

       while (foundPos != -1) {
         if (foundPos == 0 && elem.length != entry.length) {
           ret.push("<li><strong>" + elem.substr(0, entry.length) + "</strong>" +
             elem.substr(entry.length) + "</li>");
           break;
         } else if (entry.length >= instance.options.partialChars &&
           instance.options.partialSearch && foundPos != -1) {
           if (instance.options.fullSearch || /\s/.test(elem.substr(foundPos-1,1))) {
             partial.push("<li>" + elem.substr(0, foundPos) + "<strong>" +
               elem.substr(foundPos, entry.length) + "</strong>" + elem.substr(
               foundPos + entry.length) + "</li>");
             break;
           }
         }

         foundPos = instance.options.ignoreCase ?
           elem.toLowerCase().indexOf(entry.toLowerCase(), foundPos + 1) :
           elem.indexOf(entry, foundPos + 1);

       }
     }
     if (partial.length)
       ret = ret.concat(partial.slice(0, instance.options.choices - ret.length));
     return "<ul>" + ret.join('') + "</ul>";
   }
 }, options || { });
}
});

//AJAX in-place editor and collection editor
//Full rewrite by Christophe Porteneuve <tdd@tddsworld.com> (April 2007).

//Use this if you notice weird scrolling problems on some browsers,
//the DOM might be a bit confused when this gets called so do this
//waits 1 ms (with setTimeout) until it does the activation
Field.scrollFreeActivate = function(field) {
setTimeout(function() {
 Field.activate(field);
}, 1);
};

Ajax.InPlaceEditor = Class.create({
initialize: function(element, url, options) {
 this.url = url;
 this.element = element = $(element);
 this.prepareOptions();
 this._controls = { };
 arguments.callee.dealWithDeprecatedOptions(options); // DEPRECATION LAYER!!!
 Object.extend(this.options, options || { });
 if (!this.options.formId && this.element.id) {
   this.options.formId = this.element.id + '-inplaceeditor';
   if ($(this.options.formId))
     this.options.formId = '';
 }
 if (this.options.externalControl)
   this.options.externalControl = $(this.options.externalControl);
 if (!this.options.externalControl)
   this.options.externalControlOnly = false;
 this._originalBackground = this.element.getStyle('background-color') || 'transparent';
 this.element.title = this.options.clickToEditText;
 this._boundCancelHandler = this.handleFormCancellation.bind(this);
 this._boundComplete = (this.options.onComplete || Prototype.emptyFunction).bind(this);
 this._boundFailureHandler = this.handleAJAXFailure.bind(this);
 this._boundSubmitHandler = this.handleFormSubmission.bind(this);
 this._boundWrapperHandler = this.wrapUp.bind(this);
 this.registerListeners();
},
checkForEscapeOrReturn: function(e) {
 if (!this._editing || e.ctrlKey || e.altKey || e.shiftKey) return;
 if (Event.KEY_ESC == e.keyCode)
   this.handleFormCancellation(e);
 else if (Event.KEY_RETURN == e.keyCode)
   this.handleFormSubmission(e);
},
createControl: function(mode, handler, extraClasses) {
 var control = this.options[mode + 'Control'];
 var text = this.options[mode + 'Text'];
 if ('button' == control) {
   var btn = document.createElement('input');
   btn.type = 'submit';
   btn.value = text;
   btn.className = 'editor_' + mode + '_button';
   if ('cancel' == mode)
     btn.onclick = this._boundCancelHandler;
   this._form.appendChild(btn);
   this._controls[mode] = btn;
 } else if ('link' == control) {
   var link = document.createElement('a');
   link.href = '#';
   link.appendChild(document.createTextNode(text));
   link.onclick = 'cancel' == mode ? this._boundCancelHandler : this._boundSubmitHandler;
   link.className = 'editor_' + mode + '_link';
   if (extraClasses)
     link.className += ' ' + extraClasses;
   this._form.appendChild(link);
   this._controls[mode] = link;
 }
},
createEditField: function() {
 var text = (this.options.loadTextURL ? this.options.loadingText : this.getText());
 var fld;
 if (1 >= this.options.rows && !/\r|\n/.test(this.getText())) {
   fld = document.createElement('input');
   fld.type = 'text';
   var size = this.options.size || this.options.cols || 0;
   if (0 < size) fld.size = size;
 } else {
   fld = document.createElement('textarea');
   fld.rows = (1 >= this.options.rows ? this.options.autoRows : this.options.rows);
   fld.cols = this.options.cols || 40;
 }
 fld.name = this.options.paramName;
 fld.value = text; // No HTML breaks conversion anymore
 fld.className = 'editor_field';
 if (this.options.submitOnBlur)
   fld.onblur = this._boundSubmitHandler;
 this._controls.editor = fld;
 if (this.options.loadTextURL)
   this.loadExternalText();
 this._form.appendChild(this._controls.editor);
},
createForm: function() {
 var ipe = this;
 function addText(mode, condition) {
   var text = ipe.options['text' + mode + 'Controls'];
   if (!text || condition === false) return;
   ipe._form.appendChild(document.createTextNode(text));
 };
 this._form = $(document.createElement('form'));
 this._form.id = this.options.formId;
 this._form.addClassName(this.options.formClassName);
 this._form.onsubmit = this._boundSubmitHandler;
 this.createEditField();
 if ('textarea' == this._controls.editor.tagName.toLowerCase())
   this._form.appendChild(document.createElement('br'));
 if (this.options.onFormCustomization)
   this.options.onFormCustomization(this, this._form);
 addText('Before', this.options.okControl || this.options.cancelControl);
 this.createControl('ok', this._boundSubmitHandler);
 addText('Between', this.options.okControl && this.options.cancelControl);
 this.createControl('cancel', this._boundCancelHandler, 'editor_cancel');
 addText('After', this.options.okControl || this.options.cancelControl);
},
destroy: function() {
 if (this._oldInnerHTML)
   this.element.innerHTML = this._oldInnerHTML;
 this.leaveEditMode();
 this.unregisterListeners();
},
enterEditMode: function(e) {
 if (this._saving || this._editing) return;
 this._editing = true;
 this.triggerCallback('onEnterEditMode');
 if (this.options.externalControl)
   this.options.externalControl.hide();
 this.element.hide();
 this.createForm();
 this.element.parentNode.insertBefore(this._form, this.element);
 if (!this.options.loadTextURL)
   this.postProcessEditField();
 if (e) Event.stop(e);
},
enterHover: function(e) {
 if (this.options.hoverClassName)
   this.element.addClassName(this.options.hoverClassName);
 if (this._saving) return;
 this.triggerCallback('onEnterHover');
},
getText: function() {
 return this.element.innerHTML.unescapeHTML();
},
handleAJAXFailure: function(transport) {
 this.triggerCallback('onFailure', transport);
 if (this._oldInnerHTML) {
   this.element.innerHTML = this._oldInnerHTML;
   this._oldInnerHTML = null;
 }
},
handleFormCancellation: function(e) {
 this.wrapUp();
 if (e) Event.stop(e);
},
handleFormSubmission: function(e) {
 var form = this._form;
 var value = $F(this._controls.editor);
 this.prepareSubmission();
 var params = this.options.callback(form, value) || '';
 if (Object.isString(params))
   params = params.toQueryParams();
 params.editorId = this.element.id;
 if (this.options.htmlResponse) {
   var options = Object.extend({ evalScripts: true }, this.options.ajaxOptions);
   Object.extend(options, {
     parameters: params,
     onComplete: this._boundWrapperHandler,
     onFailure: this._boundFailureHandler
   });
   new Ajax.Updater({ success: this.element }, this.url, options);
 } else {
   var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
   Object.extend(options, {
     parameters: params,
     onComplete: this._boundWrapperHandler,
     onFailure: this._boundFailureHandler
   });
   new Ajax.Request(this.url, options);
 }
 if (e) Event.stop(e);
},
leaveEditMode: function() {
 this.element.removeClassName(this.options.savingClassName);
 this.removeForm();
 this.leaveHover();
 this.element.style.backgroundColor = this._originalBackground;
 this.element.show();
 if (this.options.externalControl)
   this.options.externalControl.show();
 this._saving = false;
 this._editing = false;
 this._oldInnerHTML = null;
 this.triggerCallback('onLeaveEditMode');
},
leaveHover: function(e) {
 if (this.options.hoverClassName)
   this.element.removeClassName(this.options.hoverClassName);
 if (this._saving) return;
 this.triggerCallback('onLeaveHover');
},
loadExternalText: function() {
 this._form.addClassName(this.options.loadingClassName);
 this._controls.editor.disabled = true;
 var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
 Object.extend(options, {
   parameters: 'editorId=' + encodeURIComponent(this.element.id),
   onComplete: Prototype.emptyFunction,
   onSuccess: function(transport) {
     this._form.removeClassName(this.options.loadingClassName);
     var text = transport.responseText;
     if (this.options.stripLoadedTextTags)
       text = text.stripTags();
     this._controls.editor.value = text;
     this._controls.editor.disabled = false;
     this.postProcessEditField();
   }.bind(this),
   onFailure: this._boundFailureHandler
 });
 new Ajax.Request(this.options.loadTextURL, options);
},
postProcessEditField: function() {
 var fpc = this.options.fieldPostCreation;
 if (fpc)
   $(this._controls.editor)['focus' == fpc ? 'focus' : 'activate']();
},
prepareOptions: function() {
 this.options = Object.clone(Ajax.InPlaceEditor.DefaultOptions);
 Object.extend(this.options, Ajax.InPlaceEditor.DefaultCallbacks);
 [this._extraDefaultOptions].flatten().compact().each(function(defs) {
   Object.extend(this.options, defs);
 }.bind(this));
},
prepareSubmission: function() {
 this._saving = true;
 this.removeForm();
 this.leaveHover();
 this.showSaving();
},
registerListeners: function() {
 this._listeners = { };
 var listener;
 $H(Ajax.InPlaceEditor.Listeners).each(function(pair) {
   listener = this[pair.value].bind(this);
   this._listeners[pair.key] = listener;
   if (!this.options.externalControlOnly)
     this.element.observe(pair.key, listener);
   if (this.options.externalControl)
     this.options.externalControl.observe(pair.key, listener);
 }.bind(this));
},
removeForm: function() {
 if (!this._form) return;
 this._form.remove();
 this._form = null;
 this._controls = { };
},
showSaving: function() {
 this._oldInnerHTML = this.element.innerHTML;
 this.element.innerHTML = this.options.savingText;
 this.element.addClassName(this.options.savingClassName);
 this.element.style.backgroundColor = this._originalBackground;
 this.element.show();
},
triggerCallback: function(cbName, arg) {
 if ('function' == typeof this.options[cbName]) {
   this.options[cbName](this, arg);
 }
},
unregisterListeners: function() {
 $H(this._listeners).each(function(pair) {
   if (!this.options.externalControlOnly)
     this.element.stopObserving(pair.key, pair.value);
   if (this.options.externalControl)
     this.options.externalControl.stopObserving(pair.key, pair.value);
 }.bind(this));
},
wrapUp: function(transport) {
 this.leaveEditMode();
 // Can't use triggerCallback due to backward compatibility: requires
 // binding + direct element
 this._boundComplete(transport, this.element);
}
});

Object.extend(Ajax.InPlaceEditor.prototype, {
dispose: Ajax.InPlaceEditor.prototype.destroy
});

Ajax.InPlaceCollectionEditor = Class.create(Ajax.InPlaceEditor, {
initialize: function($super, element, url, options) {
 this._extraDefaultOptions = Ajax.InPlaceCollectionEditor.DefaultOptions;
 $super(element, url, options);
},

createEditField: function() {
 var list = document.createElement('select');
 list.name = this.options.paramName;
 list.size = 1;
 this._controls.editor = list;
 this._collection = this.options.collection || [];
 if (this.options.loadCollectionURL)
   this.loadCollection();
 else
   this.checkForExternalText();
 this._form.appendChild(this._controls.editor);
},

loadCollection: function() {
 this._form.addClassName(this.options.loadingClassName);
 this.showLoadingText(this.options.loadingCollectionText);
 var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
 Object.extend(options, {
   parameters: 'editorId=' + encodeURIComponent(this.element.id),
   onComplete: Prototype.emptyFunction,
   onSuccess: function(transport) {
     var js = transport.responseText.strip();
     if (!/^\[.*\]$/.test(js))
       throw('Server returned an invalid collection representation.');
     this._collection = eval(js);
     this.checkForExternalText();
   }.bind(this),
   onFailure: this.onFailure
 });
 new Ajax.Request(this.options.loadCollectionURL, options);
},

showLoadingText: function(text) {
 this._controls.editor.disabled = true;
 var tempOption = this._controls.editor.firstChild;
 if (!tempOption) {
   tempOption = document.createElement('option');
   tempOption.value = '';
   this._controls.editor.appendChild(tempOption);
   tempOption.selected = true;
 }
 tempOption.update((text || '').stripScripts().stripTags());
},

checkForExternalText: function() {
 this._text = this.getText();
 if (this.options.loadTextURL)
   this.loadExternalText();
 else
   this.buildOptionList();
},

loadExternalText: function() {
 this.showLoadingText(this.options.loadingText);
 var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
 Object.extend(options, {
   parameters: 'editorId=' + encodeURIComponent(this.element.id),
   onComplete: Prototype.emptyFunction,
   onSuccess: function(transport) {
     this._text = transport.responseText.strip();
     this.buildOptionList();
   }.bind(this),
   onFailure: this.onFailure
 });
 new Ajax.Request(this.options.loadTextURL, options);
},

buildOptionList: function() {
 this._form.removeClassName(this.options.loadingClassName);
 this._collection = this._collection.map(function(entry) {
   return 2 === entry.length ? entry : [entry, entry].flatten();
 });
 var marker = ('value' in this.options) ? this.options.value : this._text;
 var textFound = this._collection.any(function(entry) {
   return entry[0] == marker;
 }.bind(this));
 this._controls.editor.update('');
 var option;
 this._collection.each(function(entry, index) {
   option = document.createElement('option');
   option.value = entry[0];
   option.selected = textFound ? entry[0] == marker : 0 == index;
   option.appendChild(document.createTextNode(entry[1]));
   this._controls.editor.appendChild(option);
 }.bind(this));
 this._controls.editor.disabled = false;
 Field.scrollFreeActivate(this._controls.editor);
}
});

//**** DEPRECATION LAYER FOR InPlace[Collection]Editor! ****
//**** This only  exists for a while,  in order to  let ****
//**** users adapt to  the new API.  Read up on the new ****
//**** API and convert your code to it ASAP!            ****

Ajax.InPlaceEditor.prototype.initialize.dealWithDeprecatedOptions = function(options) {
if (!options) return;
function fallback(name, expr) {
 if (name in options || expr === undefined) return;
 options[name] = expr;
};
fallback('cancelControl', (options.cancelLink ? 'link' : (options.cancelButton ? 'button' :
 options.cancelLink == options.cancelButton == false ? false : undefined)));
fallback('okControl', (options.okLink ? 'link' : (options.okButton ? 'button' :
 options.okLink == options.okButton == false ? false : undefined)));
fallback('highlightColor', options.highlightcolor);
fallback('highlightEndColor', options.highlightendcolor);
};

Object.extend(Ajax.InPlaceEditor, {
DefaultOptions: {
 ajaxOptions: { },
 autoRows: 3,                                // Use when multi-line w/ rows == 1
 cancelControl: 'link',                      // 'link'|'button'|false
 cancelText: 'cancel',
 clickToEditText: 'Click to edit',
 externalControl: null,                      // id|elt
 externalControlOnly: false,
 fieldPostCreation: 'activate',              // 'activate'|'focus'|false
 formClassName: 'inplaceeditor-form',
 formId: null,                               // id|elt
 highlightColor: '#ffff99',
 highlightEndColor: '#ffffff',
 hoverClassName: '',
 htmlResponse: true,
 loadingClassName: 'inplaceeditor-loading',
 loadingText: 'Loading...',
 okControl: 'button',                        // 'link'|'button'|false
 okText: 'ok',
 paramName: 'value',
 rows: 1,                                    // If 1 and multi-line, uses autoRows
 savingClassName: 'inplaceeditor-saving',
 savingText: 'Saving...',
 size: 0,
 stripLoadedTextTags: false,
 submitOnBlur: false,
 textAfterControls: '',
 textBeforeControls: '',
 textBetweenControls: ''
},
DefaultCallbacks: {
 callback: function(form) {
   return Form.serialize(form);
 },
 onComplete: function(transport, element) {
   // For backward compatibility, this one is bound to the IPE, and passes
   // the element directly.  It was too often customized, so we don't break it.
   new Effect.Highlight(element, {
     startcolor: this.options.highlightColor, keepBackgroundImage: true });
 },
 onEnterEditMode: null,
 onEnterHover: function(ipe) {
   ipe.element.style.backgroundColor = ipe.options.highlightColor;
   if (ipe._effect)
     ipe._effect.cancel();
 },
 onFailure: function(transport, ipe) {
   alert('Error communication with the server: ' + transport.responseText.stripTags());
 },
 onFormCustomization: null, // Takes the IPE and its generated form, after editor, before controls.
 onLeaveEditMode: null,
 onLeaveHover: function(ipe) {
   ipe._effect = new Effect.Highlight(ipe.element, {
     startcolor: ipe.options.highlightColor, endcolor: ipe.options.highlightEndColor,
     restorecolor: ipe._originalBackground, keepBackgroundImage: true
   });
 }
},
Listeners: {
 click: 'enterEditMode',
 keydown: 'checkForEscapeOrReturn',
 mouseover: 'enterHover',
 mouseout: 'leaveHover'
}
});

Ajax.InPlaceCollectionEditor.DefaultOptions = {
loadingCollectionText: 'Loading options...'
};

//Delayed observer, like Form.Element.Observer,
//but waits for delay after last key input
//Ideal for live-search fields

Form.Element.DelayedObserver = Class.create({
initialize: function(element, delay, callback) {
 this.delay     = delay || 0.5;
 this.element   = $(element);
 this.callback  = callback;
 this.timer     = null;
 this.lastValue = $F(this.element);
 Event.observe(this.element,'keyup',this.delayedListener.bindAsEventListener(this));
},
delayedListener: function(event) {
 if(this.lastValue == $F(this.element)) return;
 if(this.timer) clearTimeout(this.timer);
 this.timer = setTimeout(this.onTimerEvent.bind(this), this.delay * 1000);
 this.lastValue = $F(this.element);
},
onTimerEvent: function() {
 this.timer = null;
 this.callback(this.element, $F(this.element));
}
});
//------------------------------------------------------------------------------------------------//
// Script:dragdrop.js
//------------------------------------------------------------------------------------------------//
//script.aculo.us dragdrop.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

//Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
//script.aculo.us is freely distributable under the terms of an MIT-style license.
//For details, see the script.aculo.us web site: http://script.aculo.us/

if(Object.isUndefined(Effect))
throw("dragdrop.js requires including script.aculo.us' effects.js library");

var Droppables = {
drops: [],

remove: function(element) {
 this.drops = this.drops.reject(function(d) { return d.element==$(element) });
},

add: function(element) {
 element = $(element);
 var options = Object.extend({
   greedy:     true,
   hoverclass: null,
   tree:       false
 }, arguments[1] || { });

 // cache containers
 if(options.containment) {
   options._containers = [];
   var containment = options.containment;
   if(Object.isArray(containment)) {
     containment.each( function(c) { options._containers.push($(c)) });
   } else {
     options._containers.push($(containment));
   }
 }

 if(options.accept) options.accept = [options.accept].flatten();

 Element.makePositioned(element); // fix IE
 options.element = element;

 this.drops.push(options);
},

findDeepestChild: function(drops) {
 deepest = drops[0];

 for (var i = 1; i < drops.length; ++i)
   if (Element.isParent(drops[i].element, deepest.element))
     deepest = drops[i];

 return deepest;
},

isContained: function(element, drop) {
 var containmentNode;
 if(drop.tree) {
   containmentNode = element.treeNode;
 } else {
   containmentNode = element.parentNode;
 }
 return drop._containers.detect(function(c) { return containmentNode == c; });
},

isAffected: function(point, element, drop) {
 return (
   (drop.element!=element) &&
   ((!drop._containers) ||
     this.isContained(element, drop)) &&
   ((!drop.accept) ||
     (Element.classNames(element).detect(
       function(v) { return drop.accept.include(v);} ) )) &&
   Position.within(drop.element, point[0], point[1]) );
},

deactivate: function(drop) {
 if(drop.hoverclass)
   Element.removeClassName(drop.element, drop.hoverclass);
 this.last_active = null;
},

activate: function(drop) {
 if(drop.hoverclass)
   Element.addClassName(drop.element, drop.hoverclass);
 this.last_active = drop;
},

show: function(point, element) {
 if(!this.drops.length) return;
 var drop = null, affected = [];

 this.drops.each( function(drop) {
   if(Droppables.isAffected(point, element, drop))
     affected.push(drop);
 });

 if(affected.length>0)
   drop = Droppables.findDeepestChild(affected);

 if(this.last_active && this.last_active != drop) this.deactivate(this.last_active);
 if (drop) {
   Position.within(drop.element, point[0], point[1]);
   if(drop.onHover)
     drop.onHover(element, drop.element, Position.overlap(drop.overlap, drop.element));

   if (drop != this.last_active) Droppables.activate(drop);
 }
},

fire: function(event, element) {
 if(!this.last_active) return;
 Position.prepare();

 if (this.isAffected([Event.pointerX(event), Event.pointerY(event)], element, this.last_active))
   if (this.last_active.onDrop) {
     this.last_active.onDrop(element, this.last_active.element, event);
     return true;
   }
},

reset: function() {
 if(this.last_active)
   this.deactivate(this.last_active);
}
};

var Draggables = {
drags: [],
observers: [],

register: function(draggable) {
 if(this.drags.length == 0) {
   this.eventMouseUp   = this.endDrag.bindAsEventListener(this);
   this.eventMouseMove = this.updateDrag.bindAsEventListener(this);
   this.eventKeypress  = this.keyPress.bindAsEventListener(this);

   Event.observe(document, "mouseup", this.eventMouseUp);
   Event.observe(document, "mousemove", this.eventMouseMove);
   Event.observe(document, "keypress", this.eventKeypress);
 }
 this.drags.push(draggable);
},

unregister: function(draggable) {
 this.drags = this.drags.reject(function(d) { return d==draggable; });
 if(this.drags.length == 0) {
   Event.stopObserving(document, "mouseup", this.eventMouseUp);
   Event.stopObserving(document, "mousemove", this.eventMouseMove);
   Event.stopObserving(document, "keypress", this.eventKeypress);
 }
},

activate: function(draggable) {
 if(draggable.options.delay) {
   this._timeout = setTimeout(function() {
     Draggables._timeout = null;
     window.focus();
     Draggables.activeDraggable = draggable;
   }.bind(this), draggable.options.delay);
 } else {
   window.focus(); // allows keypress events if window isn't currently focused, fails for Safari
   this.activeDraggable = draggable;
 }
},

deactivate: function() {
 this.activeDraggable = null;
},

updateDrag: function(event) {
 if(!this.activeDraggable) return;
 var pointer = [Event.pointerX(event), Event.pointerY(event)];
 // Mozilla-based browsers fire successive mousemove events with
 // the same coordinates, prevent needless redrawing (moz bug?)
 if(this._lastPointer && (this._lastPointer.inspect() == pointer.inspect())) return;
 this._lastPointer = pointer;

 this.activeDraggable.updateDrag(event, pointer);
},

endDrag: function(event) {
 if(this._timeout) {
   clearTimeout(this._timeout);
   this._timeout = null;
 }
 if(!this.activeDraggable) return;
 this._lastPointer = null;
 this.activeDraggable.endDrag(event);
 this.activeDraggable = null;
},

keyPress: function(event) {
 if(this.activeDraggable)
   this.activeDraggable.keyPress(event);
},

addObserver: function(observer) {
 this.observers.push(observer);
 this._cacheObserverCallbacks();
},

removeObserver: function(element) {  // element instead of observer fixes mem leaks
 this.observers = this.observers.reject( function(o) { return o.element==element; });
 this._cacheObserverCallbacks();
},

notify: function(eventName, draggable, event) {  // 'onStart', 'onEnd', 'onDrag'
 if(this[eventName+'Count'] > 0)
   this.observers.each( function(o) {
     if(o[eventName]) o[eventName](eventName, draggable, event);
   });
 if(draggable.options[eventName]) draggable.options[eventName](draggable, event);
},

_cacheObserverCallbacks: function() {
 ['onStart','onEnd','onDrag'].each( function(eventName) {
   Draggables[eventName+'Count'] = Draggables.observers.select(
     function(o) { return o[eventName]; }
   ).length;
 });
}
};

/*--------------------------------------------------------------------------*/

var Draggable = Class.create({
initialize: function(element) {
 var defaults = {
   handle: false,
   reverteffect: function(element, top_offset, left_offset) {
     var dur = Math.sqrt(Math.abs(top_offset^2)+Math.abs(left_offset^2))*0.02;
     new Effect.Move(element, { x: -left_offset, y: -top_offset, duration: dur,
       queue: {scope:'_draggable', position:'end'}
     });
   },
   endeffect: function(element) {
     var toOpacity = Object.isNumber(element._opacity) ? element._opacity : 1.0;
     new Effect.Opacity(element, {duration:0.2, from:0.7, to:toOpacity,
       queue: {scope:'_draggable', position:'end'},
       afterFinish: function(){
         Draggable._dragging[element] = false;
       }
     });
   },
   zindex: 1000,
   revert: false,
   quiet: false,
   scroll: false,
   scrollSensitivity: 20,
   scrollSpeed: 15,
   snap: false,  // false, or xy or [x,y] or function(x,y){ return [x,y] }
   delay: 0
 };

 if(!arguments[1] || Object.isUndefined(arguments[1].endeffect))
   Object.extend(defaults, {
     starteffect: function(element) {
       element._opacity = Element.getOpacity(element);
       Draggable._dragging[element] = true;
       new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
     }
   });

 var options = Object.extend(defaults, arguments[1] || { });

 this.element = $(element);

 if(options.handle && Object.isString(options.handle))
   this.handle = this.element.down('.'+options.handle, 0);

 if(!this.handle) this.handle = $(options.handle);
 if(!this.handle) this.handle = this.element;

 if(options.scroll && !options.scroll.scrollTo && !options.scroll.outerHTML) {
   options.scroll = $(options.scroll);
   this._isScrollChild = Element.childOf(this.element, options.scroll);
 }

 Element.makePositioned(this.element); // fix IE

 this.options  = options;
 this.dragging = false;

 this.eventMouseDown = this.initDrag.bindAsEventListener(this);
 Event.observe(this.handle, "mousedown", this.eventMouseDown);

 Draggables.register(this);
},

destroy: function() {
 Event.stopObserving(this.handle, "mousedown", this.eventMouseDown);
 Draggables.unregister(this);
},

currentDelta: function() {
 return([
   parseInt(Element.getStyle(this.element,'left') || '0'),
   parseInt(Element.getStyle(this.element,'top') || '0')]);
},

initDrag: function(event) {
 if(!Object.isUndefined(Draggable._dragging[this.element]) &&
   Draggable._dragging[this.element]) return;
 if(Event.isLeftClick(event)) {
   // abort on form elements, fixes a Firefox issue
   var src = Event.element(event);
   if((tag_name = src.tagName.toUpperCase()) && (
     tag_name=='INPUT' ||
     tag_name=='SELECT' ||
     tag_name=='OPTION' ||
     tag_name=='BUTTON' ||
     tag_name=='TEXTAREA')) return;

   var pointer = [Event.pointerX(event), Event.pointerY(event)];
   var pos     = this.element.cumulativeOffset();
   this.offset = [0,1].map( function(i) { return (pointer[i] - pos[i]); });

   Draggables.activate(this);
   Event.stop(event);
 }
},

startDrag: function(event) {
 this.dragging = true;
 if(!this.delta)
   this.delta = this.currentDelta();

 if(this.options.zindex) {
   this.originalZ = parseInt(Element.getStyle(this.element,'z-index') || 0);
   this.element.style.zIndex = this.options.zindex;
 }

 if(this.options.ghosting) {
   this._clone = this.element.cloneNode(true);
   this._originallyAbsolute = (this.element.getStyle('position') == 'absolute');
   if (!this._originallyAbsolute)
     Position.absolutize(this.element);
   this.element.parentNode.insertBefore(this._clone, this.element);
 }

 if(this.options.scroll) {
   if (this.options.scroll == window) {
     var where = this._getWindowScroll(this.options.scroll);
     this.originalScrollLeft = where.left;
     this.originalScrollTop = where.top;
   } else {
     this.originalScrollLeft = this.options.scroll.scrollLeft;
     this.originalScrollTop = this.options.scroll.scrollTop;
   }
 }

 Draggables.notify('onStart', this, event);

 if(this.options.starteffect) this.options.starteffect(this.element);
},

updateDrag: function(event, pointer) {
 if(!this.dragging) this.startDrag(event);

 if(!this.options.quiet){
   Position.prepare();
   Droppables.show(pointer, this.element);
 }

 Draggables.notify('onDrag', this, event);

 this.draw(pointer);
 if(this.options.change) this.options.change(this);

 if(this.options.scroll) {
   this.stopScrolling();

   var p;
   if (this.options.scroll == window) {
     with(this._getWindowScroll(this.options.scroll)) { p = [ left, top, left+width, top+height ]; }
   } else {
     p = Position.page(this.options.scroll);
     p[0] += this.options.scroll.scrollLeft + Position.deltaX;
     p[1] += this.options.scroll.scrollTop + Position.deltaY;
     p.push(p[0]+this.options.scroll.offsetWidth);
     p.push(p[1]+this.options.scroll.offsetHeight);
   }
   var speed = [0,0];
   if(pointer[0] < (p[0]+this.options.scrollSensitivity)) speed[0] = pointer[0]-(p[0]+this.options.scrollSensitivity);
   if(pointer[1] < (p[1]+this.options.scrollSensitivity)) speed[1] = pointer[1]-(p[1]+this.options.scrollSensitivity);
   if(pointer[0] > (p[2]-this.options.scrollSensitivity)) speed[0] = pointer[0]-(p[2]-this.options.scrollSensitivity);
   if(pointer[1] > (p[3]-this.options.scrollSensitivity)) speed[1] = pointer[1]-(p[3]-this.options.scrollSensitivity);
   this.startScrolling(speed);
 }

 // fix AppleWebKit rendering
 if(Prototype.Browser.WebKit) window.scrollBy(0,0);

 Event.stop(event);
},

finishDrag: function(event, success) {
 this.dragging = false;

 if(this.options.quiet){
   Position.prepare();
   var pointer = [Event.pointerX(event), Event.pointerY(event)];
   Droppables.show(pointer, this.element);
 }

 if(this.options.ghosting) {
   if (!this._originallyAbsolute)
     Position.relativize(this.element);
   delete this._originallyAbsolute;
   Element.remove(this._clone);
   this._clone = null;
 }

 var dropped = false;
 if(success) {
   dropped = Droppables.fire(event, this.element);
   if (!dropped) dropped = false;
 }
 if(dropped && this.options.onDropped) this.options.onDropped(this.element);
 Draggables.notify('onEnd', this, event);

 var revert = this.options.revert;
 if(revert && Object.isFunction(revert)) revert = revert(this.element);

 var d = this.currentDelta();
 if(revert && this.options.reverteffect) {
   if (dropped == 0 || revert != 'failure')
     this.options.reverteffect(this.element,
       d[1]-this.delta[1], d[0]-this.delta[0]);
 } else {
   this.delta = d;
 }

 if(this.options.zindex)
   this.element.style.zIndex = this.originalZ;

 if(this.options.endeffect)
   this.options.endeffect(this.element);

 Draggables.deactivate(this);
 Droppables.reset();
},

keyPress: function(event) {
 if(event.keyCode!=Event.KEY_ESC) return;
 this.finishDrag(event, false);
 Event.stop(event);
},

endDrag: function(event) {
 if(!this.dragging) return;
 this.stopScrolling();
 this.finishDrag(event, true);
 Event.stop(event);
},

draw: function(point) {
 var pos = this.element.cumulativeOffset();
 if(this.options.ghosting) {
   var r   = Position.realOffset(this.element);
   pos[0] += r[0] - Position.deltaX; pos[1] += r[1] - Position.deltaY;
 }

 var d = this.currentDelta();
 pos[0] -= d[0]; pos[1] -= d[1];

 if(this.options.scroll && (this.options.scroll != window && this._isScrollChild)) {
   pos[0] -= this.options.scroll.scrollLeft-this.originalScrollLeft;
   pos[1] -= this.options.scroll.scrollTop-this.originalScrollTop;
 }

 var p = [0,1].map(function(i){
   return (point[i]-pos[i]-this.offset[i]);
 }.bind(this));

 if(this.options.snap) {
   if(Object.isFunction(this.options.snap)) {
     p = this.options.snap(p[0],p[1],this);
   } else {
   if(Object.isArray(this.options.snap)) {
     p = p.map( function(v, i) {
       return (v/this.options.snap[i]).round()*this.options.snap[i]; }.bind(this));
   } else {
     p = p.map( function(v) {
       return (v/this.options.snap).round()*this.options.snap; }.bind(this));
   }
 }}

 var style = this.element.style;
 if((!this.options.constraint) || (this.options.constraint=='horizontal'))
   style.left = p[0] + "px";
 if((!this.options.constraint) || (this.options.constraint=='vertical'))
   style.top  = p[1] + "px";

 if(style.visibility=="hidden") style.visibility = ""; // fix gecko rendering
},

stopScrolling: function() {
 if(this.scrollInterval) {
   clearInterval(this.scrollInterval);
   this.scrollInterval = null;
   Draggables._lastScrollPointer = null;
 }
},

startScrolling: function(speed) {
 if(!(speed[0] || speed[1])) return;
 this.scrollSpeed = [speed[0]*this.options.scrollSpeed,speed[1]*this.options.scrollSpeed];
 this.lastScrolled = new Date();
 this.scrollInterval = setInterval(this.scroll.bind(this), 10);
},

scroll: function() {
 var current = new Date();
 var delta = current - this.lastScrolled;
 this.lastScrolled = current;
 if(this.options.scroll == window) {
   with (this._getWindowScroll(this.options.scroll)) {
     if (this.scrollSpeed[0] || this.scrollSpeed[1]) {
       var d = delta / 1000;
       this.options.scroll.scrollTo( left + d*this.scrollSpeed[0], top + d*this.scrollSpeed[1] );
     }
   }
 } else {
   this.options.scroll.scrollLeft += this.scrollSpeed[0] * delta / 1000;
   this.options.scroll.scrollTop  += this.scrollSpeed[1] * delta / 1000;
 }

 Position.prepare();
 Droppables.show(Draggables._lastPointer, this.element);
 Draggables.notify('onDrag', this);
 if (this._isScrollChild) {
   Draggables._lastScrollPointer = Draggables._lastScrollPointer || $A(Draggables._lastPointer);
   Draggables._lastScrollPointer[0] += this.scrollSpeed[0] * delta / 1000;
   Draggables._lastScrollPointer[1] += this.scrollSpeed[1] * delta / 1000;
   if (Draggables._lastScrollPointer[0] < 0)
     Draggables._lastScrollPointer[0] = 0;
   if (Draggables._lastScrollPointer[1] < 0)
     Draggables._lastScrollPointer[1] = 0;
   this.draw(Draggables._lastScrollPointer);
 }

 if(this.options.change) this.options.change(this);
},

_getWindowScroll: function(w) {
 var T=null, L=null, W=null, H=null;
 with (w.document) {
   if (w.document.documentElement && documentElement.scrollTop) {
     T = documentElement.scrollTop;
     L = documentElement.scrollLeft;
   } else if (w.document.body) {
     T = body.scrollTop;
     L = body.scrollLeft;
   }
   if (w.innerWidth) {
     W = w.innerWidth;
     H = w.innerHeight;
   } else if (w.document.documentElement && documentElement.clientWidth) {
     W = documentElement.clientWidth;
     H = documentElement.clientHeight;
   } else {
     W = body.offsetWidth;
     H = body.offsetHeight;
   }
 }
 return { top: T, left: L, width: W, height: H };
}
});

Draggable._dragging = { };

/*--------------------------------------------------------------------------*/

var SortableObserver = Class.create({
initialize: function(element, observer) {
 this.element   = $(element);
 this.observer  = observer;
 this.lastValue = Sortable.serialize(this.element);
},

onStart: function() {
 this.lastValue = Sortable.serialize(this.element);
},

onEnd: function() {
 Sortable.unmark();
 if(this.lastValue != Sortable.serialize(this.element))
   this.observer(this.element);
}
});

var Sortable = {
SERIALIZE_RULE: /^[^_\-](?:[A-Za-z0-9\-\_]*)[_](.*)$/,

sortables: { },

_findRootElement: function(element) {
 while (element.tagName.toUpperCase() != "BODY") {
   if(element.id && Sortable.sortables[element.id]) return element;
   element = element.parentNode;
 }
},

options: function(element) {
 element = Sortable._findRootElement($(element));
 if(!element) return;
 return Sortable.sortables[element.id];
},

destroy: function(element){
 element = $(element);
 var s = Sortable.sortables[element.id];

 if(s) {
   Draggables.removeObserver(s.element);
   s.droppables.each(function(d){ Droppables.remove(d); });
   s.draggables.invoke('destroy');

   delete Sortable.sortables[s.element.id];
 }
},

create: function(element) {
 element = $(element);
 var options = Object.extend({
   element:     element,
   tag:         'li',       // assumes li children, override with tag: 'tagname'
   dropOnEmpty: false,
   tree:        false,
   treeTag:     'ul',
   overlap:     'vertical', // one of 'vertical', 'horizontal'
   constraint:  'vertical', // one of 'vertical', 'horizontal', false
   containment: element,    // also takes array of elements (or id's); or false
   handle:      false,      // or a CSS class
   only:        false,
   delay:       0,
   hoverclass:  null,
   ghosting:    false,
   quiet:       false,
   scroll:      false,
   scrollSensitivity: 20,
   scrollSpeed: 15,
   format:      this.SERIALIZE_RULE,

   // these take arrays of elements or ids and can be
   // used for better initialization performance
   elements:    false,
   handles:     false,

   onChange:    Prototype.emptyFunction,
   onUpdate:    Prototype.emptyFunction
 }, arguments[1] || { });

 // clear any old sortable with same element
 this.destroy(element);

 // build options for the draggables
 var options_for_draggable = {
   revert:      true,
   quiet:       options.quiet,
   scroll:      options.scroll,
   scrollSpeed: options.scrollSpeed,
   scrollSensitivity: options.scrollSensitivity,
   delay:       options.delay,
   ghosting:    options.ghosting,
   constraint:  options.constraint,
   handle:      options.handle };

 if(options.starteffect)
   options_for_draggable.starteffect = options.starteffect;

 if(options.reverteffect)
   options_for_draggable.reverteffect = options.reverteffect;
 else
   if(options.ghosting) options_for_draggable.reverteffect = function(element) {
     element.style.top  = 0;
     element.style.left = 0;
   };

 if(options.endeffect)
   options_for_draggable.endeffect = options.endeffect;

 if(options.zindex)
   options_for_draggable.zindex = options.zindex;

 // build options for the droppables
 var options_for_droppable = {
   overlap:     options.overlap,
   containment: options.containment,
   tree:        options.tree,
   hoverclass:  options.hoverclass,
   onHover:     Sortable.onHover
 };

 var options_for_tree = {
   onHover:      Sortable.onEmptyHover,
   overlap:      options.overlap,
   containment:  options.containment,
   hoverclass:   options.hoverclass
 };

 // fix for gecko engine
 Element.cleanWhitespace(element);

 options.draggables = [];
 options.droppables = [];

 // drop on empty handling
 if(options.dropOnEmpty || options.tree) {
   Droppables.add(element, options_for_tree);
   options.droppables.push(element);
 }

 (options.elements || this.findElements(element, options) || []).each( function(e,i) {
   var handle = options.handles ? $(options.handles[i]) :
     (options.handle ? $(e).select('.' + options.handle)[0] : e);
   options.draggables.push(
     new Draggable(e, Object.extend(options_for_draggable, { handle: handle })));
   Droppables.add(e, options_for_droppable);
   if(options.tree) e.treeNode = element;
   options.droppables.push(e);
 });

 if(options.tree) {
   (Sortable.findTreeElements(element, options) || []).each( function(e) {
     Droppables.add(e, options_for_tree);
     e.treeNode = element;
     options.droppables.push(e);
   });
 }

 // keep reference
 this.sortables[element.identify()] = options;

 // for onupdate
 Draggables.addObserver(new SortableObserver(element, options.onUpdate));

},

// return all suitable-for-sortable elements in a guaranteed order
findElements: function(element, options) {
 return Element.findChildren(
   element, options.only, options.tree ? true : false, options.tag);
},

findTreeElements: function(element, options) {
 return Element.findChildren(
   element, options.only, options.tree ? true : false, options.treeTag);
},

onHover: function(element, dropon, overlap) {
 if(Element.isParent(dropon, element)) return;

 if(overlap > .33 && overlap < .66 && Sortable.options(dropon).tree) {
   return;
 } else if(overlap>0.5) {
   Sortable.mark(dropon, 'before');
   if(dropon.previousSibling != element) {
     var oldParentNode = element.parentNode;
     element.style.visibility = "hidden"; // fix gecko rendering
     dropon.parentNode.insertBefore(element, dropon);
     if(dropon.parentNode!=oldParentNode)
       Sortable.options(oldParentNode).onChange(element);
     Sortable.options(dropon.parentNode).onChange(element);
   }
 } else {
   Sortable.mark(dropon, 'after');
   var nextElement = dropon.nextSibling || null;
   if(nextElement != element) {
     var oldParentNode = element.parentNode;
     element.style.visibility = "hidden"; // fix gecko rendering
     dropon.parentNode.insertBefore(element, nextElement);
     if(dropon.parentNode!=oldParentNode)
       Sortable.options(oldParentNode).onChange(element);
     Sortable.options(dropon.parentNode).onChange(element);
   }
 }
},

onEmptyHover: function(element, dropon, overlap) {
 var oldParentNode = element.parentNode;
 var droponOptions = Sortable.options(dropon);

 if(!Element.isParent(dropon, element)) {
   var index;

   var children = Sortable.findElements(dropon, {tag: droponOptions.tag, only: droponOptions.only});
   var child = null;

   if(children) {
     var offset = Element.offsetSize(dropon, droponOptions.overlap) * (1.0 - overlap);

     for (index = 0; index < children.length; index += 1) {
       if (offset - Element.offsetSize (children[index], droponOptions.overlap) >= 0) {
         offset -= Element.offsetSize (children[index], droponOptions.overlap);
       } else if (offset - (Element.offsetSize (children[index], droponOptions.overlap) / 2) >= 0) {
         child = index + 1 < children.length ? children[index + 1] : null;
         break;
       } else {
         child = children[index];
         break;
       }
     }
   }

   dropon.insertBefore(element, child);

   Sortable.options(oldParentNode).onChange(element);
   droponOptions.onChange(element);
 }
},

unmark: function() {
 if(Sortable._marker) Sortable._marker.hide();
},

mark: function(dropon, position) {
 // mark on ghosting only
 var sortable = Sortable.options(dropon.parentNode);
 if(sortable && !sortable.ghosting) return;

 if(!Sortable._marker) {
   Sortable._marker =
     ($('dropmarker') || Element.extend(document.createElement('DIV'))).
       hide().addClassName('dropmarker').setStyle({position:'absolute'});
   document.getElementsByTagName("body").item(0).appendChild(Sortable._marker);
 }
 var offsets = dropon.cumulativeOffset();
 Sortable._marker.setStyle({left: offsets[0]+'px', top: offsets[1] + 'px'});

 if(position=='after')
   if(sortable.overlap == 'horizontal')
     Sortable._marker.setStyle({left: (offsets[0]+dropon.clientWidth) + 'px'});
   else
     Sortable._marker.setStyle({top: (offsets[1]+dropon.clientHeight) + 'px'});

 Sortable._marker.show();
},

_tree: function(element, options, parent) {
 var children = Sortable.findElements(element, options) || [];

 for (var i = 0; i < children.length; ++i) {
   var match = children[i].id.match(options.format);

   if (!match) continue;

   var child = {
     id: encodeURIComponent(match ? match[1] : null),
     element: element,
     parent: parent,
     children: [],
     position: parent.children.length,
     container: $(children[i]).down(options.treeTag)
   };

   /* Get the element containing the children and recurse over it */
   if (child.container)
     this._tree(child.container, options, child);

   parent.children.push (child);
 }

 return parent;
},

tree: function(element) {
 element = $(element);
 var sortableOptions = this.options(element);
 var options = Object.extend({
   tag: sortableOptions.tag,
   treeTag: sortableOptions.treeTag,
   only: sortableOptions.only,
   name: element.id,
   format: sortableOptions.format
 }, arguments[1] || { });

 var root = {
   id: null,
   parent: null,
   children: [],
   container: element,
   position: 0
 };

 return Sortable._tree(element, options, root);
},

/* Construct a [i] index for a particular node */
_constructIndex: function(node) {
 var index = '';
 do {
   if (node.id) index = '[' + node.position + ']' + index;
 } while ((node = node.parent) != null);
 return index;
},

sequence: function(element) {
 element = $(element);
 var options = Object.extend(this.options(element), arguments[1] || { });

 return $(this.findElements(element, options) || []).map( function(item) {
   return item.id.match(options.format) ? item.id.match(options.format)[1] : '';
 });
},

setSequence: function(element, new_sequence) {
 element = $(element);
 var options = Object.extend(this.options(element), arguments[2] || { });

 var nodeMap = { };
 this.findElements(element, options).each( function(n) {
     if (n.id.match(options.format))
         nodeMap[n.id.match(options.format)[1]] = [n, n.parentNode];
     n.parentNode.removeChild(n);
 });

 new_sequence.each(function(ident) {
   var n = nodeMap[ident];
   if (n) {
     n[1].appendChild(n[0]);
     delete nodeMap[ident];
   }
 });
},

serialize: function(element) {
 element = $(element);
 var options = Object.extend(Sortable.options(element), arguments[1] || { });
 var name = encodeURIComponent(
   (arguments[1] && arguments[1].name) ? arguments[1].name : element.id);

 if (options.tree) {
   return Sortable.tree(element, arguments[1]).children.map( function (item) {
     return [name + Sortable._constructIndex(item) + "[id]=" +
             encodeURIComponent(item.id)].concat(item.children.map(arguments.callee));
   }).flatten().join('&');
 } else {
   return Sortable.sequence(element, arguments[1]).map( function(item) {
     return name + "[]=" + encodeURIComponent(item);
   }).join('&');
 }
}
};

//Returns true if child is contained within element
Element.isParent = function(child, element) {
if (!child.parentNode || child == element) return false;
if (child.parentNode == element) return true;
return Element.isParent(child.parentNode, element);
};

Element.findChildren = function(element, only, recursive, tagName) {
if(!element.hasChildNodes()) return null;
tagName = tagName.toUpperCase();
if(only) only = [only].flatten();
var elements = [];
$A(element.childNodes).each( function(e) {
 if(e.tagName && e.tagName.toUpperCase()==tagName &&
   (!only || (Element.classNames(e).detect(function(v) { return only.include(v); }))))
     elements.push(e);
 if(recursive) {
   var grandchildren = Element.findChildren(e, only, recursive, tagName);
   if(grandchildren) elements.push(grandchildren);
 }
});

return (elements.length>0 ? elements.flatten() : []);
};

Element.offsetSize = function (element, type) {
return element['offset' + ((type=='vertical' || type=='height') ? 'Height' : 'Width')];
};
//------------------------------------------------------------------------------------------------//
// Script:menu.js
//------------------------------------------------------------------------------------------------//
//** All Levels Navigational Menu- (c) Dynamic Drive DHTML code library: http://www.dynamicdrive.com
//** Script Download/ instructions page: http://www.dynamicdrive.com/dynamicindex1/ddlevelsmenu/
//** Usage Terms: http://www.dynamicdrive.com/notice.htm

//** Current version: 2.2 See changelog.txt for details

if (typeof dd_domreadycheck=="undefined") //global variable to detect if DOM is ready
	var dd_domreadycheck=false;

var ddlevelsmenu={

enableshim: true, //enable IFRAME shim to prevent drop down menus from being hidden below SELECT or FLASH elements? (tip: disable if not in use, for efficiency)

arrowpointers:{
	downarrow: ["./img/menu/arrowdown.gif",7,5], //[path_to_down_arrow, arrowwidth, arrowheight]
	rightarrow: ["./img/menu/arrowright.gif",5,7],//[path_to_right_arrow, arrowwidth, arrowheight]
	showarrow: {toplevel: true, sublevel: true} //Show arrow images on top level items and sub level items, respectively?
},
hideinterval: 100, //delay in milliseconds before entire menu disappears onmouseout.
effects: {enableswipe: true, enablefade: true, duration: 200},
httpsiframesrc: "blank.htm", //If menu is run on a secure (https) page, the IFRAME shim feature used by the script should point to an *blank* page *within* the secure area to prevent an IE security prompt. Specify full URL to that page on your server (leave as is if not applicable).

///No need to edit beyond here////////////////////

topmenuids: [], //array containing ids of all the primary menus on the page
topitems: {}, //object array containing all top menu item links
subuls: {}, //object array containing all ULs
lastactivesubul: {}, //object object containing info for last mouse out menu item's UL
topitemsindex: -1,
ulindex: -1,
hidetimers: {}, //object array timer
shimadded: false,
nonFF: !/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent), //detect non FF browsers
getoffset:function(what, offsettype){
	return (what.offsetParent)? what[offsettype]+this.getoffset(what.offsetParent, offsettype) : what[offsettype];
},

getoffsetof:function(el){
	el._offsets={left:this.getoffset(el, "offsetLeft"), top:this.getoffset(el, "offsetTop")};
},

getwindowsize:function(){
	this.docwidth=window.innerWidth? window.innerWidth-10 : this.standardbody.clientWidth-10;
	this.docheight=window.innerHeight? window.innerHeight-15 : this.standardbody.clientHeight-18;
},

gettopitemsdimensions:function(){
	for (var m=0; m<this.topmenuids.length; m++){
		var topmenuid=this.topmenuids[m];
		for (var i=0; i<this.topitems[topmenuid].length; i++){
			var header=this.topitems[topmenuid][i];
			var submenu=document.getElementById(header.getAttribute('rel'));
			header._dimensions={w:header.offsetWidth, h:header.offsetHeight, submenuw:submenu.offsetWidth, submenuh:submenu.offsetHeight};
		}
	}
},

isContained:function(m, e){
	var e=window.event || e;
	var c=e.relatedTarget || ((e.type=="mouseover")? e.fromElement : e.toElement);
	while (c && c!=m)try {c=c.parentNode;} catch(e){c=m;}
	if (c==m)
		return true;
	else
		return false;
},

addpointer:function(target, imgclass, imginfo, BeforeorAfter){
	var pointer=document.createElement("img");
	pointer.src=imginfo[0];
	pointer.style.width=imginfo[1]+"px";
	pointer.style.height=imginfo[2]+"px";
	if(imgclass=="rightarrowpointer"){
		pointer.style.left=target.offsetWidth-imginfo[2]-2+"px";
	}
	pointer.className=imgclass;
	var target_firstEl=target.childNodes[target.firstChild.nodeType!=1? 1 : 0]; //see if the first child element within A is a SPAN (found in sliding doors technique)
	if (target_firstEl && target_firstEl.tagName=="SPAN"){
		target=target_firstEl; //arrow should be added inside this SPAN instead if found
	}
	if (BeforeorAfter=="before")
		target.insertBefore(pointer, target.firstChild);
	else
		target.appendChild(pointer);
},

css:function(el, targetclass, action){
	var needle=new RegExp("(^|\\s+)"+targetclass+"($|\\s+)", "ig");
	if (action=="check")
		return needle.test(el.className);
	else if (action=="remove")
		el.className=el.className.replace(needle, "");
	else if (action=="add" && !needle.test(el.className))
		el.className+=" "+targetclass;
},

addshimmy:function(target){
	var shim=(!window.opera)? document.createElement("iframe") : document.createElement("div"); //Opera 9.24 doesnt seem to support transparent IFRAMEs
	shim.className="ddiframeshim";
	shim.setAttribute("src", location.protocol=="https:"? this.httpsiframesrc : "about:blank");
	shim.setAttribute("frameborder", "0");
	target.appendChild(shim);
	try{
		shim.style.filter='progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
	}
	catch(e){}
	return shim;
},

positionshim:function(header, submenu, dir, scrollX, scrollY){
	if (header._istoplevel){
		var scrollY=window.pageYOffset? window.pageYOffset : this.standardbody.scrollTop;
		var topgap=header._offsets.top-scrollY;
		var bottomgap=scrollY+this.docheight-header._offsets.top-header._dimensions.h;
		if (topgap>0){
			this.shimmy.topshim.style.left=scrollX+"px";
			this.shimmy.topshim.style.top=scrollY+"px";
			this.shimmy.topshim.style.width="99%";
			this.shimmy.topshim.style.height=topgap+"px"; //distance from top window edge to top of menu item
		}
		if (bottomgap>0){
			this.shimmy.bottomshim.style.left=scrollX+"px";
			this.shimmy.bottomshim.style.top=header._offsets.top + header._dimensions.h +"px";
			this.shimmy.bottomshim.style.width="99%";
			this.shimmy.bottomshim.style.height=bottomgap+"px"; //distance from bottom of menu item to bottom window edge
		}
	}
},

hideshim:function(){
	this.shimmy.topshim.style.width=this.shimmy.bottomshim.style.width=0;
	this.shimmy.topshim.style.height=this.shimmy.bottomshim.style.height=0;
},


buildmenu:function(mainmenuid, header, submenu, submenupos, istoplevel, dir){
	header._master=mainmenuid; //Indicate which top menu this header is associated with
	header._pos=submenupos; //Indicate pos of sub menu this header is associated with
	header._istoplevel=istoplevel;
	if (istoplevel){
		this.addEvent(header, function(e){
		ddlevelsmenu.hidemenu(ddlevelsmenu.subuls[this._master][parseInt(this._pos)]);
		}, "click");
	}
	this.subuls[mainmenuid][submenupos]=submenu;
	header._dimensions={w:header.offsetWidth, h:header.offsetHeight, submenuw:submenu.offsetWidth, submenuh:submenu.offsetHeight};
	this.getoffsetof(header);
	submenu.style.left=0;
	submenu.style.top=0;
	submenu.style.visibility="hidden";
	this.addEvent(header, function(e){ //mouseover event
		if (!ddlevelsmenu.isContained(this, e)){
			var submenu=ddlevelsmenu.subuls[this._master][parseInt(this._pos)];
			if (this._istoplevel){
				ddlevelsmenu.css(this, "selected", "add");
			clearTimeout(ddlevelsmenu.hidetimers[this._master][this._pos]);
			}
			ddlevelsmenu.getoffsetof(header);
			var scrollX=window.pageXOffset? window.pageXOffset : ddlevelsmenu.standardbody.scrollLeft;
			var scrollY=window.pageYOffset? window.pageYOffset : ddlevelsmenu.standardbody.scrollTop;
			var submenurightedge=this._offsets.left + this._dimensions.submenuw + (this._istoplevel && dir=="topbar"? 0 : this._dimensions.w);
			var submenubottomedge=this._offsets.top + this._dimensions.submenuh;
			//Sub menu starting left position
			var menuleft=(this._istoplevel? this._offsets.left + (dir=="sidebar"? this._dimensions.w : 0) : this._dimensions.w);
			if (submenurightedge-scrollX>ddlevelsmenu.docwidth){
				menuleft+= -this._dimensions.submenuw + (this._istoplevel && dir=="topbar" ? this._dimensions.w : -this._dimensions.w);
			}
			submenu.style.left=menuleft+"px";
			//Sub menu starting top position
			var menutop=(this._istoplevel? this._offsets.top + (dir=="sidebar"? 0 : this._dimensions.h) : this.offsetTop);
			if (submenubottomedge-scrollY>ddlevelsmenu.docheight){ //no room downwards?
				if (this._dimensions.submenuh<this._offsets.top+(dir=="sidebar"? this._dimensions.h : 0)-scrollY){ //move up?
					menutop+= - this._dimensions.submenuh + (this._istoplevel && dir=="topbar"? -this._dimensions.h : this._dimensions.h);
				}
				else{ //top of window edge
					menutop+= -(this._offsets.top-scrollY) + (this._istoplevel && dir=="topbar"? -this._dimensions.h : 0);
				}
			}
			menutop = (menutop == 56 || menutop == 22 || menutop == 0 ? menutop - 1 : menutop);
			submenu.style.top=menutop+"px";
			if (ddlevelsmenu.enableshim && (ddlevelsmenu.effects.enableswipe==false || ddlevelsmenu.nonFF)){ //apply shim immediately only if animation is turned off, or if on, in non FF2.x browsers
				ddlevelsmenu.positionshim(header, submenu, dir, scrollX, scrollY);
			}
			else{
				submenu.FFscrollInfo={x:scrollX, y:scrollY};
			}
			ddlevelsmenu.showmenu(header, submenu, dir);
		}
	}, "mouseover");
	this.addEvent(header, function(e){ //mouseout event
		var submenu=ddlevelsmenu.subuls[this._master][parseInt(this._pos)];
		if (this._istoplevel){
			if (!ddlevelsmenu.isContained(this, e) && !ddlevelsmenu.isContained(submenu, e)) //hide drop down ul if mouse moves out of menu bar item but not into drop down ul itself
				ddlevelsmenu.hidemenu(submenu);
		}
		else if (!this._istoplevel && !ddlevelsmenu.isContained(this, e)){
			ddlevelsmenu.hidemenu(submenu);
		}

	}, "mouseout");
},

setopacity:function(el, value){
	el.style.opacity=value;
	if (typeof el.style.opacity!="string"){ //if it's not a string (ie: number instead), it means property not supported
		el.style.MozOpacity=value;
		if (el.filters){
			el.style.filter="progid:DXImageTransform.Microsoft.alpha(opacity="+ value*100 +")";
		}
	}
},

showmenu:function(header, submenu, dir){
	if (this.effects.enableswipe || this.effects.enablefade){
		if (this.effects.enableswipe){
			var endpoint=(header._istoplevel && dir=="topbar")? header._dimensions.submenuh : header._dimensions.submenuw
			submenu.style.width=submenu.style.height=0;
			submenu.style.overflow="hidden";
		}
		if (this.effects.enablefade){
			this.setopacity(submenu, 0); //set opacity to 0 so menu appears hidden initially
		}
		submenu._curanimatedegree=0;
		submenu.style.visibility="visible";
		clearInterval(submenu._animatetimer);
		submenu._starttime=new Date().getTime(); //get time just before animation is run
		submenu._animatetimer=setInterval(function(){ddlevelsmenu.revealmenu(header, submenu, endpoint, dir);}, 10)
	}
	else{
		submenu.style.visibility="visible";
	}
},

revealmenu:function(header, submenu, endpoint, dir){
	var elapsed=new Date().getTime()-submenu._starttime; //get time animation has run
	if (elapsed<this.effects.duration){
		if (this.effects.enableswipe){
			if (submenu._curanimatedegree==0){ //reset either width or height of sub menu to "auto" when animation begins
				submenu.style[header._istoplevel && dir=="topbar"? "width" : "height"]="auto";
			}
			submenu.style[header._istoplevel && dir=="topbar"? "height" : "width"]=(submenu._curanimatedegree*endpoint)+"px";
		}
		if (this.effects.enablefade){
			this.setopacity(submenu, submenu._curanimatedegree);
		}
	}
	else{
		clearInterval(submenu._animatetimer);
		if (this.effects.enableswipe){
			submenu.style.width="auto";
			submenu.style.height="auto";
			submenu.style.overflow="visible";
		}
		if (this.effects.enablefade){
			this.setopacity(submenu, 1);
			submenu.style.filter="";
		}
		if (this.enableshim && submenu.FFscrollInfo) //if this is FF browser (meaning shim hasn't been applied yet
			this.positionshim(header, submenu, dir, submenu.FFscrollInfo.x, submenu.FFscrollInfo.y);
	}
	submenu._curanimatedegree=(1-Math.cos((elapsed/this.effects.duration)*Math.PI));
},

hidemenu:function(submenu){
	if (typeof submenu._pos!="undefined"){ //if submenu is outermost UL drop down menu
		this.css(this.topitems[submenu._master][parseInt(submenu._pos)], "selected", "remove");
		if (this.enableshim)
			this.hideshim();
	}
	clearInterval(submenu._animatetimer);
	submenu.style.left=0;
	submenu.style.top="-1000px";
	submenu.style.visibility="hidden";
},


addEvent:function(target, functionref, tasktype) {
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false);
	else if (target.attachEvent)
		target.attachEvent('on'+tasktype, function(){return functionref.call(target, window.event);});
},

domready:function(functionref){ //based on code from the jQuery library
	if (dd_domreadycheck){
		functionref();
		return
	}
	// Mozilla, Opera and webkit nightlies currently support this event
	if (document.addEventListener) {
		// Use the handy event callback
		document.addEventListener("DOMContentLoaded", function(){
			document.removeEventListener("DOMContentLoaded", arguments.callee, false);
			functionref();
			dd_domreadycheck=true;
		}, false );
	}
	else if (document.attachEvent){
		// If IE and not an iframe
		// continually check to see if the document is ready
		if ( document.documentElement.doScroll && window == window.top) (function(){
			if (dd_domreadycheck){
				functionref();
				return
			}
			try{
				// If IE is used, use the trick by Diego Perini
				// http://javascript.nwbox.com/IEContentLoaded/
				document.documentElement.doScroll("left");
			}catch(error){
				setTimeout( arguments.callee, 0);
				return;
			}
			//and execute any waiting functions
			functionref();
			dd_domreadycheck=true;
		})();
	}
	if (document.attachEvent && parent.length>0) //account for page being in IFRAME, in which above doesn't fire in IE
		this.addEvent(window, function(){functionref();}, "load");
},


init:function(mainmenuid, dir){
	this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body;
	this.topitemsindex=-1;
	this.ulindex=-1;
	this.topmenuids.push(mainmenuid);
	this.topitems[mainmenuid]=[]; //declare array on object
	this.subuls[mainmenuid]=[]; //declare array on object
	this.hidetimers[mainmenuid]=[]; //declare hide entire menu timer
	if (this.enableshim && !this.shimadded){
		this.shimmy={};
		this.shimmy.topshim=this.addshimmy(document.body); //create top iframe shim obj
		this.shimmy.bottomshim=this.addshimmy(document.body); //create bottom iframe shim obj
		this.shimadded=true;
	}
	var menubar=document.getElementById(mainmenuid);
	var alllinks=menubar.getElementsByTagName("a");
	this.getwindowsize();
	for (var i=0; i<alllinks.length; i++){
		if (alllinks[i].getAttribute('rel')){
			this.topitemsindex++;
			this.ulindex++;
			var menuitem=alllinks[i];
			this.topitems[mainmenuid][this.topitemsindex]=menuitem; //store ref to main menu links
			var dropul=document.getElementById(menuitem.getAttribute('rel'));
			document.body.appendChild(dropul); //move main ULs to end of document
			dropul.style.zIndex=2000; //give drop down menus a high z-index
			dropul._master=mainmenuid;  //Indicate which main menu this main UL is associated with
			dropul._pos=this.topitemsindex; //Indicate which main menu item this main UL is associated with
			this.addEvent(dropul, function(){ddlevelsmenu.hidemenu(this);}, "click");
			var arrowclass=(dir=="sidebar")? "rightarrowpointer" : "downarrowpointer";
			var arrowpointer=(dir=="sidebar")? this.arrowpointers.rightarrow : this.arrowpointers.downarrow;
			if (this.arrowpointers.showarrow.toplevel)
				this.addpointer(menuitem, arrowclass, arrowpointer, (dir=="sidebar")? "before" : "after");
			this.buildmenu(mainmenuid, menuitem, dropul, this.ulindex, true, dir); //build top level menu
			dropul.onmouseover=function(){
				clearTimeout(ddlevelsmenu.hidetimers[this._master][this._pos]);
			};
			this.addEvent(dropul, function(e){ //hide menu if mouse moves out of main UL element into open space
				if (!ddlevelsmenu.isContained(this, e) && !ddlevelsmenu.isContained(ddlevelsmenu.topitems[this._master][parseInt(this._pos)], e)){
					var dropul=this;
					if (ddlevelsmenu.enableshim)
						ddlevelsmenu.hideshim();
					ddlevelsmenu.hidetimers[this._master][this._pos]=setTimeout(function(){
						ddlevelsmenu.hidemenu(dropul);
					}, ddlevelsmenu.hideinterval);
				}
			}, "mouseout");
			var subuls=dropul.getElementsByTagName("ul");
			for (var c=0; c<subuls.length; c++){
				this.ulindex++;
				var parentli=subuls[c].parentNode;
				if (this.arrowpointers.showarrow.sublevel)
					this.addpointer(parentli.getElementsByTagName("a")[0], "rightarrowpointer", this.arrowpointers.rightarrow, "before");
				this.buildmenu(mainmenuid, parentli, subuls[c], this.ulindex, false, dir); //build sub level menus
			}
		}
	} //end for loop
	this.addEvent(window, function(){ddlevelsmenu.getwindowsize(); ddlevelsmenu.gettopitemsdimensions();}, "resize");
},

setup:function(mainmenuid, dir){
	this.domready(function(){ddlevelsmenu.init(mainmenuid, dir);});
}

};
//------------------------------------------------------------------------------------------------//
// Script:scriptaculous.js
//------------------------------------------------------------------------------------------------//
//script.aculo.us scriptaculous.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

//Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
//Permission is hereby granted, free of charge, to any person obtaining
//a copy of this software and associated documentation files (the
//"Software"), to deal in the Software without restriction, including
//without limitation the rights to use, copy, modify, merge, publish,
//distribute, sublicense, and/or sell copies of the Software, and to
//permit persons to whom the Software is furnished to do so, subject to
//the following conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
//OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
//WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
//For details, see the script.aculo.us web site: http://script.aculo.us/

var Scriptaculous = {
Version: '1.8.3',
require: function(libraryName) {
 try{
   // inserting via DOM fails in Safari 2.0, so brute force approach
   document.write('<script type="text/javascript" src="'+libraryName+'"><\/script>');
 } catch(e) {
   // for xhtml+xml served content, fall back to DOM methods
   var script = document.createElement('script');
   script.type = 'text/javascript';
   script.src = libraryName;
   document.getElementsByTagName('head')[0].appendChild(script);
 }
},
REQUIRED_PROTOTYPE: '1.6.0.3',
load: function() {
 function convertVersionString(versionString) {
   var v = versionString.replace(/_.*|\./g, '');
   v = parseInt(v + '0'.times(4-v.length));
   return versionString.indexOf('_') > -1 ? v-1 : v;
 }

 if((typeof Prototype=='undefined') ||
    (typeof Element == 'undefined') ||
    (typeof Element.Methods=='undefined') ||
    (convertVersionString(Prototype.Version) <
     convertVersionString(Scriptaculous.REQUIRED_PROTOTYPE)))
    throw("script.aculo.us requires the Prototype JavaScript framework >= " +
     Scriptaculous.REQUIRED_PROTOTYPE);

 var js = /scriptaculous\.js(\?.*)?$/;
 $$('head script[src]').findAll(function(s) {
   return s.src.match(js);
 }).each(function(s) {
   var path = s.src.replace(js, ''),
   includes = s.src.match(/\?.*load=([a-z,]*)/);
   (includes ? includes[1] : 'builder,effects,dragdrop,controls,slider,sound').split(',').each(
    function(include) { Scriptaculous.require(path+include+'.js'); });
 });
}
};

Scriptaculous.load();
//------------------------------------------------------------------------------------------------//
// Script:selectmultiple.js
//------------------------------------------------------------------------------------------------//
/**
 * @author Ryan Johnson <http://syntacticx.com/>
 * @copyright 2008 PersonalGrid Corporation <http://personalgrid.com/>
 * @package LivePipe UI
 * @license MIT
 * @url http://livepipe.net/control/rating
 * @require prototype.js, livepipe.js
 */

/*global Prototype, Class, Option, $, $A, Control, $break,  */

if(typeof(Prototype) == "undefined") {
    throw "Control.SelectMultiple requires Prototype to be loaded."; }
if(typeof(Object.Event) == "undefined") {
    throw "Control.SelectMultiple requires Object.Event to be loaded."; }

Control.SelectMultiple = Class.create({
    select: false,
    container: false,
    numberOfCheckedBoxes: 0,
    checkboxes: [],
    hasExtraOption: false,
    initialize: function(select,container,options){
        this.options = {
            checkboxSelector: 'input[type=checkbox]',
            nameSelector: 'span.name',
            labelSeparator: ', ',
            valueSeparator: ',',
            afterChange: Prototype.emptyFunction,
            overflowString: function(str){
                return str.truncate();
            },
            overflowLength: 30
        };
        Object.extend(this.options,options || {});
        this.select = $(select);
		this.currentIndex = null;
		this.currentValue = null;
		this.currentLabel = null;
        this.container =  $(container);
        this.checkboxes = (typeof(this.options.checkboxSelector) == 'function') ? 
            this.options.checkboxSelector.bind(this)() : 
            this.container.getElementsBySelector(this.options.checkboxSelector);
        var value_was_set = false;
        if(this.options.value){
            value_was_set = true;
            this.setValue(this.options.value);
            delete this.options.value;
        }
        this.hasExtraOption = false;
        this.checkboxes.each(function(checkbox){
         checkbox.observe('click',this.checkboxOnClick.bind(this,checkbox));
        }.bind(this));
        this.select.observe('change',this.selectOnChange.bind(this));
        this.countAndCheckCheckBoxes();
        if(!value_was_set) {
         this.scanCheckBoxes(); }
        this.notify('afterChange',this.select.options.selectedIndex != -1?this.select.options[this.select.options.selectedIndex].value:null);
    },
	set: function(){
		this.currentIndex = this.select.options.selectedIndex;
		this.currentValue = this.select.options[this.select.options.selectedIndex].value;
		this.currentLabel = this.select.options[this.select.options.selectedIndex].text;
	},
	reset: function(){
		var lastIndex = this.select.options.length - 1;
		this.select.options.selectedIndex = this.currentIndex;
		if (this.currentIndex < lastIndex) {
			this.select.options[lastIndex] = null;
		}
		else {
			this.select.options[lastIndex].value = this.currentValue;
			this.select.options[lastIndex].text = '--- Custom [' + this.currentValue.split(',').length + '] ---';
		}
	},
    countAndCheckCheckBoxes: function(){
        this.numberOfCheckedBoxes = this.checkboxes.inject(0,function(number,checkbox){
            checkbox.checked = (this.select.options[this.select.options.selectedIndex].value == checkbox.value);
            var value_string = this.select.options[this.select.options.selectedIndex].value;
            var value_collection = $A(value_string.split ? value_string.split(this.options.valueSeparator) : value_string);
            var should_check = value_collection.any(function(value) {
                if (!should_check && checkbox.value == value) {
                    return true; }
            }.bind(this));
            checkbox.checked = should_check;
            if(checkbox.checked) {
                ++number; }
            return number;
        }.bind(this));
    },
    setValue: function(value_string){
        this.numberOfCheckedBoxes = 0;
        var value_collection = $A(value_string.split ? value_string.split(this.options.valueSeparator) : value_string);
        this.checkboxes.each(function(checkbox){
            checkbox.checked = false;
            value_collection.each(function(value){
                if(checkbox.value == value){
                    ++this.numberOfCheckedBoxes;
                    checkbox.checked = true;
                }
            }.bind(this));
        }.bind(this));
        this.scanCheckBoxes();
    },
    selectOnChange: function(){
        this.countAndCheckCheckBoxes();
        this.notify('afterChange',this.select.options[this.select.options.selectedIndex].value);
    },
    checkboxOnClick: function(checkbox){
        this.numberOfCheckedBoxes = this.checkboxes.findAll(function (c) { 
            return c.checked; 
        }).length;
        this.scanCheckBoxes();
        this.notify('afterChange', this.numberOfCheckedBoxes === 0 ? "" :
            this.select.options[this.select.options.selectedIndex].value);
    },
    scanCheckBoxes: function(){
        switch(this.numberOfCheckedBoxes){
            case 1:
                this.checkboxes.each(function(checkbox){
                    if(checkbox.checked){
                        $A(this.select.options).each(function(option,i){
                            if(option.value == checkbox.value){
                                this.select.options.selectedIndex = i;
                                throw $break;
                            }
                        }.bind(this));
                        throw $break;
                    }
                }.bind(this));
                break;
            case 0:
                break;
            default:
                this.addExtraOption();
                break;
        }
    },
    getLabelsForExtraOption: function(){
        var labels = (typeof(this.options.nameSelector) == 'function' ? 
            this.options.nameSelector.bind(this)() : 
            this.container.getElementsBySelector(this.options.nameSelector).inject([],function(labels,name_element,i){
                if(this.checkboxes[i].checked) {
                    labels.push(name_element.innerHTML); }
                return labels;
            }.bind(this))
        );
		return (labels);
    },
    getValueForExtraOption: function(){
        return this.checkboxes.inject([],function(values,checkbox){
            if(checkbox.checked) {
                values.push(checkbox.value); }
            return values;
        }).join(this.options.valueSeparator);
    },
    addExtraOption: function(){
        this.hasExtraOption = true;
		var labels = this.getLabelsForExtraOption();
		var txt = '--- Custom [' + labels.length + '] ---';
		var opt = this.select.options[this.select.options.length - 1];
		if (opt.value.indexOf(',') == -1) {
			opt = new Option('', '');
			this.select.options[this.select.options.length] = opt;
		}
		opt.value = this.getValueForExtraOption();
		opt.text = txt;
        this.select.options.selectedIndex = this.select.options.length - 1;
    },
    removeExtraOption: function(){
    }
});
Object.Event.extend(Control.SelectMultiple);
//------------------------------------------------------------------------------------------------//
// Script:sound.js
//------------------------------------------------------------------------------------------------//
// script.aculo.us sound.js v1.8.3, Thu Oct 08 11:23:33 +0200 2009

// Copyright (c) 2005-2009 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
// Based on code created by Jules Gravinese (http://www.webveteran.com/)
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

Sound = {
  tracks: {},
  _enabled: true,
  template:
    new Template('<embed style="height:0" id="sound_#{track}_#{id}" src="#{url}" loop="false" autostart="true" hidden="true"/>'),
  enable: function(){
    Sound._enabled = true;
  },
  disable: function(){
    Sound._enabled = false;
  },
  play: function(url){
    if(!Sound._enabled) return;
    var options = Object.extend({
      track: 'global', url: url, replace: false
    }, arguments[1] || {});

    if(options.replace && this.tracks[options.track]) {
      $R(0, this.tracks[options.track].id).each(function(id){
        var sound = $('sound_'+options.track+'_'+id);
        sound.Stop && sound.Stop();
        sound.remove();
      });
      this.tracks[options.track] = null;
    }

    if(!this.tracks[options.track])
      this.tracks[options.track] = { id: 0 };
    else
      this.tracks[options.track].id++;

    options.id = this.tracks[options.track].id;
    $$('body')[0].insert(
      Prototype.Browser.IE ? new Element('bgsound',{
        id: 'sound_'+options.track+'_'+options.id,
        src: options.url, loop: 1, autostart: true
      }) : Sound.template.evaluate(options));
  }
};

if(Prototype.Browser.Gecko && navigator.userAgent.indexOf("Win") > 0){
  if(navigator.plugins && $A(navigator.plugins).detect(function(p){ return p.name.indexOf('QuickTime') != -1 }))
    Sound.template = new Template('<object id="sound_#{track}_#{id}" width="0" height="0" type="audio/mpeg" data="#{url}"/>');
  else if(navigator.plugins && $A(navigator.plugins).detect(function(p){ return p.name.indexOf('Windows Media') != -1 }))
    Sound.template = new Template('<object id="sound_#{track}_#{id}" type="application/x-mplayer2" data="#{url}"></object>');
  else if(navigator.plugins && $A(navigator.plugins).detect(function(p){ return p.name.indexOf('RealPlayer') != -1 }))
    Sound.template = new Template('<embed type="audio/x-pn-realaudio-plugin" style="height:0" id="sound_#{track}_#{id}" src="#{url}" loop="false" autostart="true" hidden="true"/>');
  else
    Sound.play = function(){};
}
//------------------------------------------------------------------------------------------------//
// Script:tabcontrol.js
//------------------------------------------------------------------------------------------------//
/**
 * @author Ryan Johnson <http://syntacticx.com/>
 * @copyright 2008 PersonalGrid Corporation <http://personalgrid.com/>
 * @package LivePipe UI
 * @license MIT
 * @url http://livepipe.net/control/tabs
 * @require prototype.js, livepipe.js
 */

/*global window, document, Prototype, $, $A, $H, $break, Class, Element, Event, Control */

if(typeof(Prototype) == "undefined") {
    throw "Control.Tabs requires Prototype to be loaded."; }
if(typeof(Object.Event) == "undefined") {
    throw "Control.Tabs requires Object.Event to be loaded."; }

Control.Tabs = Class.create({
    initialize: function(tab_list_container,options){
        if(!$(tab_list_container)) {
            throw "Control.Tabs could not find the element: " + tab_list_container; }
        this.activeContainer = false;
        this.activeLink = false;
        this.containers = $H({});
        this.links = [];
        Control.Tabs.instances.push(this);
        this.options = {
            beforeChange: Prototype.emptyFunction,
            afterChange: Prototype.emptyFunction,
            hover: false,
            linkSelector: 'li a',
            setClassOnContainer: false,
            activeClassName: 'active',
            defaultTab: 'first',
            autoLinkExternal: true,
            targetRegExp: /#(.+)$/,
            showFunction: Element.show,
            hideFunction: Element.hide
        };
        Object.extend(this.options,options || {});
        (typeof(this.options.linkSelector == 'string') ? 
            $(tab_list_container).select(this.options.linkSelector) : 
            this.options.linkSelector($(tab_list_container))
        ).findAll(function(link){
            return (/^#/).exec((Prototype.Browser.WebKit ? decodeURIComponent(link.href) : link.href).replace(window.location.href.split('#')[0],''));
        }).each(function(link){
            this.addTab(link);
        }.bind(this));
        this.containers.values().each(Element.hide);
        if(this.options.defaultTab == 'first') {
            this.setActiveTab(this.links.first());
        } else if(this.options.defaultTab == 'last') {
            this.setActiveTab(this.links.last());
        } else {
            this.setActiveTab(this.options.defaultTab); }
        var targets = this.options.targetRegExp.exec(window.location);
        if(targets && targets[1]){
            targets[1].split(',').each(function(target){
                this.setActiveTab(this.links.find(function(link){
                    return link.key == target;
                }));
            }.bind(this));
        }
        if(this.options.autoLinkExternal){
            $A(document.getElementsByTagName('a')).each(function(a){
                if(!this.links.include(a)){
                    var clean_href = a.href.replace(window.location.href.split('#')[0],'');
                    if(clean_href.substring(0,1) == '#'){
                        if(this.containers.keys().include(clean_href.substring(1))){
                            $(a).observe('click',function(event,clean_href){
                                this.setActiveTab(clean_href.substring(1));
                            }.bindAsEventListener(this,clean_href));
                        }
                    }
                }
            }.bind(this));
        }
    },
    addTab: function(link){
        this.links.push(link);
        link.key = link.getAttribute('href').replace(window.location.href.split('#')[0],'').split('#').last().replace(/#/,'');
        var container = $(link.key);
        if(!container) {
            throw "Control.Tabs: #" + link.key + " was not found on the page."; }
        this.containers.set(link.key,container);
        link[this.options.hover ? 'onmouseover' : 'onclick'] = function(link){
            if(window.event) {
                Event.stop(window.event); }
            this.setActiveTab(link);
            return false;
        }.bind(this,link);
    },
	removeTab: function(key){
		var t = new Array();
		for (var i = 0; i < this.links.length ; i++) {
			if (this.links[i].key != key) {
				t.push(this.links[i]);
			}
		}
		this.links = t;
		$(key).remove();
	},
	isEmpty: function(){
		return (this.links.length == 0);
	},
    setActiveTab: function(link){
        if(!link && typeof(link) == 'undefined') {
            return; }
        if(typeof(link) == 'string'){
            this.setActiveTab(this.links.find(function(_link){
                return _link.key == link;
            }));
        }else if(typeof(link) == 'number'){
            this.setActiveTab(this.links[link]);
        }else{
            if(this.notify('beforeChange',this.activeContainer,this.containers.get(link.key)) === false) {
                return; }
            if(this.activeContainer) {
                this.options.hideFunction(this.activeContainer); }
            this.links.each(function(item){
                (this.options.setClassOnContainer ? $(item.parentNode) : item).removeClassName(this.options.activeClassName);
            }.bind(this));
            (this.options.setClassOnContainer ? $(link.parentNode) : link).addClassName(this.options.activeClassName);
            this.activeContainer = this.containers.get(link.key);
            this.activeLink = link;
            this.options.showFunction(this.containers.get(link.key));
            this.notify('afterChange',this.containers.get(link.key));
        }
    },
    next: function(){
        this.links.each(function(link,i){
            if(this.activeLink == link && this.links[i + 1]){
                this.setActiveTab(this.links[i + 1]);
                throw $break;
            }
        }.bind(this));
    },
    previous: function(){
        this.links.each(function(link,i){
            if(this.activeLink == link && this.links[i - 1]){
                this.setActiveTab(this.links[i - 1]);
                throw $break;
            }
        }.bind(this));
    },
    first: function(){
        this.setActiveTab(this.links.first());
    },
    last: function(){
        this.setActiveTab(this.links.last());
    }
});
Object.extend(Control.Tabs,{
    instances: [],
    findByTabId: function(id){
        return Control.Tabs.instances.find(function(tab){
            return tab.links.find(function(link){
                return link.key == id;
            });
        });
    }
});
Object.Event.extend(Control.Tabs);
//------------------------------------------------------------------------------------------------//
// Script:treeview.js
//------------------------------------------------------------------------------------------------//
//Title: Tigra Tree
//Description: See the demo at url
//URL: http://www.softcomplex.com/products/tigra_menu_tree/
//Version: 1.1 (size optimized)
//Date: 11-12-2002 (mm-dd-yyyy)
//Notes: This script is free. Visit official site for further details.
var treeTemplate = {
	'target'  : '_self',
	'icon_e'  : 'img/component/treeview/empty.gif',
	'icon_l'  : 'img/component/treeview/line.gif',
	'icon_32' : 'img/menu/database.png',
	'icon_36' : 'img/menu/database.png',
	'icon_48' : 'img/menu/database.png',
	'icon_52' : 'img/menu/database.png',
	'icon_56' : 'img/menu/database.png',
	'icon_60' : 'img/menu/database.png',
	'icon_16' : 'img/component/treeview/folder.gif',
	'icon_20' : 'img/component/treeview/folder.gif',
	'icon_24' : 'img/component/treeview/folder.gif',
	'icon_28' : 'img/component/treeview/folder.gif',
	'icon_0'  : 'img/component/treeview/page.png',
	'icon_4'  : 'img/component/treeview/page.png',
	'icon_2'  : 'img/component/treeview/joinbottom.gif',
	'icon_3'  : 'img/component/treeview/join.gif',
	'icon_17' : 'img/component/treeview/plustop.gif',
	'icon_18' : 'img/component/treeview/plus.gif',
	'icon_19' : 'img/component/treeview/plusbottom.gif',
	'icon_25' : 'img/component/treeview/minustop.gif',
	'icon_26' : 'img/component/treeview/minus.gif',
	'icon_27' : 'img/component/treeview/minusbottom.gif'
};
function Tree(a_items, a_template) {
	this.a_tpl      = a_template;
	this.a_config   = a_items;
	this.o_root     = this;
	this.a_index    = [];
	this.o_selected = null;
	this.n_depth    = -1;
	
	var o_icone = new Image(),
		o_iconl = new Image();
	o_icone.src = a_template['icon_e'];
	o_iconl.src = a_template['icon_l'];
	a_template['im_e'] = o_icone;
	a_template['im_l'] = o_iconl;
	for (var i = 0; i < 64; i++)
		if (a_template['icon_' + i]) {
			var o_icon = new Image();
			a_template['im_' + i] = o_icon;
			o_icon.src = a_template['icon_' + i];
		}
	
	this.toggle = function (n_id) {	var o_item = this.a_index[n_id]; o_item.open(o_item.b_opened); };
	this.select = function (n_id) { return this.a_index[n_id].select(); };
	this.mout   = function (n_id) { this.a_index[n_id].upstatus(true); };
	this.mover  = function (n_id) { this.a_index[n_id].upstatus(); };

	this.a_children = [];
	for (var i = 0; i < a_items.length; i++)
		new tree_item(this, i);

	this.n_id = trees.length;
	trees[this.n_id] = this;
	
	for (var i = 0; i < this.a_children.length; i++) {
		document.write(this.a_children[i].init());
		this.a_children[i].open();
	}
}
function tree_item(o_parent, n_order) {
	this.n_depth  = o_parent.n_depth + 1;
	this.a_config = o_parent.a_config[n_order + (this.n_depth ? 2 : 0)];
	if (!this.a_config) return;

	this.o_root    = o_parent.o_root;
	this.o_parent  = o_parent;
	this.n_order   = n_order;
	this.b_opened  = !this.n_depth;

	this.n_id = this.o_root.a_index.length;
	this.o_root.a_index[this.n_id] = this;
	o_parent.a_children[n_order] = this;

	this.a_children = [];
	for (var i = 0; i < this.a_config.length - 2; i++)
		new tree_item(this, i);

	this.get_icon = item_get_icon;
	this.open     = item_open;
	this.select   = item_select;
	this.init     = item_init;
	this.upstatus = item_upstatus;
	this.is_last  = function () { return this.n_order == this.o_parent.a_children.length - 1;};
}
function item_open(b_close) {
	var o_idiv = get_element('i_div' + this.o_root.n_id + '_' + this.n_id);
	if (!o_idiv) return;
	
	if (!o_idiv.innerHTML) {
		var a_children = [];
		for (var i = 0; i < this.a_children.length; i++)
			a_children[i]= this.a_children[i].init();
		o_idiv.innerHTML = a_children.join('');
	}
	o_idiv.style.display = (b_close ? 'none' : 'block');
	refreshEvents(o_idiv.id);
	
	this.b_opened = !b_close;
	var o_jicon = document.images['j_img' + this.o_root.n_id + '_' + this.n_id],
		o_iicon = document.images['i_img' + this.o_root.n_id + '_' + this.n_id];
	if (o_jicon) o_jicon.src = (o_jicon.src.indexOf('plustop.gif') != -1 ? this.o_root.a_tpl['icon_25'] : (o_jicon.src.indexOf('minustop.gif') != -1 ? this.o_root.a_tpl['icon_17'] : this.get_icon(true)));
	if (o_iicon) o_iicon.src = this.get_icon();
	this.upstatus();
}
function item_select(b_deselect) {
	if (!b_deselect) {
		var o_olditem = this.o_root.o_selected;
		this.o_root.o_selected = this;
		if (o_olditem) o_olditem.select(true);
	}
	var o_iicon = document.images['i_img' + this.o_root.n_id + '_' + this.n_id];
	if (o_iicon) o_iicon.src = this.get_icon();
	this.upstatus();
	return Boolean(this.a_config[1]);
}
function item_upstatus(b_clear) {
	//window.setTimeout('window.status="' + (b_clear ? '' : this.a_config[0] + (this.a_config[1] ? ' ('+ this.a_config[1] + ')' : '')) + '"', 10);
}
function item_init() {
	var a_offset = [],
		o_current_item = this.o_parent;
	for (var i = this.n_depth; i > 1; i--) {
		a_offset[i] = '<img src="' + this.o_root.a_tpl[o_current_item.is_last() ? 'icon_e' : 'icon_l'] + '" border="0" align="absbottom">';
		o_current_item = o_current_item.o_parent;
	}
	var onclick = ' onclick=' + (this.a_children.length ? '"javascript: trees[' + this.o_root.n_id + '].toggle(' + this.n_id + ')"' : '"treeLeafClick(this,\'' + this.a_config[1] + '\');"');
	var display = (!this.n_depth ? 'style="display:none;"' : '');
	return '<table ' + display + ' cellpadding="0" cellspacing="0" border="0"><tr' + onclick + '><td nowrap>' + (this.n_depth ? a_offset.join('') + (this.a_children.length
			? '<img src="' + this.get_icon(true) + '" border="0" style="cursor:pointer;" align="absbottom" name="j_img' + this.o_root.n_id + '_' + this.n_id + '">'
			: '<img src="' + this.get_icon(true) + '" border="0" align="absbottom">') : '')
			+ '<img src="' + this.get_icon() + '" border="0" style="cursor:pointer;" align="absbottom" name="i_img' + this.o_root.n_id + '_' + this.n_id + '" class="t' + this.o_root.n_id + 'im"></td><td class="nodetext"><span>' + this.a_config[0] + '</span></td></tr></table>' + (this.a_children.length ? '<div id="i_div' + this.o_root.n_id + '_' + this.n_id + '" style="display:none"></div>' : '');
}
var firstDone = false;
function item_get_icon(b_junction) {
	var index = ((this.n_depth ? 0 : 32) + (this.a_children.length ? 16 : 0) + (this.a_children.length && this.b_opened ? 8 : 0) + (!b_junction && this.o_root.o_selected == this ? 4 : 0) + (b_junction ? 2 : 0) + (b_junction && this.is_last() ? 1 : 0));
	if (parseInt(index) == 18 && !firstDone) {
		index = '17';
		firstDone = true;
	}
	return this.o_root.a_tpl['icon_' + index];
}
function refreshEvents(id) {
	var t = $$('#' + id + ' span');
	for (var i = 0 ; i < t.length ; i++) {
		t[i].onmouseover = function(){this.addClassName('over');};
		t[i].onmouseout = function(){this.removeClassName('over');};
	}
}
var trees = [];
get_element = (document.all ?
	function (s_id) { return document.all[s_id]; } :
	function (s_id) { return document.getElementById(s_id); });
//------------------------------------------------------------------------------------------------//
// Script:window.js
//------------------------------------------------------------------------------------------------//
/**
 * @author Ryan Johnson <http://syntacticx.com/>
 * @copyright 2008 PersonalGrid Corporation <http://personalgrid.com/>
 * @package LivePipe UI
 * @license MIT
 * @url http://livepipe.net/control/window
 * @require prototype.js, effects.js, draggable.js, resizable.js, livepipe.js
 */
//adds onDraw and constrainToViewport option to draggable
if(typeof(Draggable) != 'undefined'){
    //allows the point to be modified with an onDraw callback
    Draggable.prototype.draw = function(point) {
        var pos = Position.cumulativeOffset(this.element);
        if(this.options.ghosting) {
            var r = Position.realOffset(this.element);
            pos[0] += r[0] - Position.deltaX; pos[1] += r[1] - Position.deltaY;
        }
        
        var d = this.currentDelta();
        pos[0] -= d[0]; pos[1] -= d[1];
        
        if(this.options.scroll && (this.options.scroll != window && this._isScrollChild)) {
            pos[0] -= this.options.scroll.scrollLeft-this.originalScrollLeft;
            pos[1] -= this.options.scroll.scrollTop-this.originalScrollTop;
        }
        
        var p = [0,1].map(function(i){ 
            return (point[i]-pos[i]-this.offset[i]) 
        }.bind(this));
        
        if(this.options.snap) {
            if(typeof this.options.snap == 'function') {
                p = this.options.snap(p[0],p[1],this);
            } else {
                if(this.options.snap instanceof Array) {
                    p = p.map( function(v, i) {return Math.round(v/this.options.snap[i])*this.options.snap[i]; }.bind(this));
                } else {
                    p = p.map( function(v) {return Math.round(v/this.options.snap)*this.options.snap; }.bind(this));
                  }
            }
        }
        
        if(this.options.onDraw)
            this.options.onDraw.bind(this)(p);
        else{
            var style = this.element.style;
            if(this.options.constrainToViewport){
                var viewport_dimensions = document.viewport.getDimensions();
                var container_dimensions = this.element.getDimensions();
                var margin_top = parseInt(this.element.getStyle('margin-top'));
                var margin_left = parseInt(this.element.getStyle('margin-left'));
                var boundary = [[
                    0 - margin_left,
                    0 - margin_top
                ],[
                    (viewport_dimensions.width - container_dimensions.width) - margin_left,
                    (viewport_dimensions.height - container_dimensions.height) - margin_top
                ]];
                if((!this.options.constraint) || (this.options.constraint=='horizontal')){ 
                    if((p[0] >= boundary[0][0]) && (p[0] <= boundary[1][0]))
                        this.element.style.left = p[0] + "px";
                    else
                        this.element.style.left = ((p[0] < boundary[0][0]) ? boundary[0][0] : boundary[1][0]) + "px";
                } 
                if((!this.options.constraint) || (this.options.constraint=='vertical')){ 
                    if((p[1] >= boundary[0][1] ) && (p[1] <= boundary[1][1]))
                        this.element.style.top = p[1] + "px";
                  else
                        this.element.style.top = ((p[1] <= boundary[0][1]) ? boundary[0][1] : boundary[1][1]) + "px";               
                }
            }else{
                if((!this.options.constraint) || (this.options.constraint=='horizontal'))
                  style.left = p[0] + "px";
                if((!this.options.constraint) || (this.options.constraint=='vertical'))
                  style.top     = p[1] + "px";
            }
            if(style.visibility=="hidden")
                style.visibility = ""; // fix gecko rendering
        }
    };
}

if(typeof(Prototype) == "undefined")
    throw "Control.Window requires Prototype to be loaded.";
if(typeof(IframeShim) == "undefined")
    throw "Control.Window requires IframeShim to be loaded.";
if(typeof(Object.Event) == "undefined")
    throw "Control.Window requires Object.Event to be loaded.";
/*
    known issues:
        - when iframe is clicked is does not gain focus
        - safari can't open multiple iframes properly
        - constrainToViewport: body must have no margin or padding for this to work properly
        - iframe will be mis positioned during fade in
        - document.viewport does not account for scrollbars (this will eventually be fixed in the prototype core)
    notes
        - setting constrainToViewport only works when the page is not scrollable
        - setting draggable: true will negate the effects of position: center
*/
Control.Window = Class.create({
    initialize: function(container,options){
        Control.Window.windows.push(this);
        
        //attribute initialization
        this.container = false;
        this.isOpen = false;
        this.href = false;
        this.sourceContainer = false; //this is optionally the container that will open the window
        this.ajaxRequest = false;
        this.remoteContentLoaded = false; //this is set when the code to load the remote content is run, onRemoteContentLoaded is fired when the connection is closed
        this.numberInSequence = Control.Window.windows.length + 1; //only useful for the effect scoping
        this.indicator = false;
        this.effects = {
            fade: false,
            appear: false
        };
        this.indicatorEffects = {
            fade: false,
            appear: false
        };
        
        //options
        this.options = Object.extend({
            //lifecycle
            beforeOpen: Prototype.emptyFunction,
            afterOpen: Prototype.emptyFunction,
            beforeClose: Prototype.emptyFunction,
            afterClose: Prototype.emptyFunction,
            //dimensions and modes
            height: null,
            width: null,
            className: false,
            position: 'center', //'center', 'relative', [x,y], [function(){return x;},function(){return y;}]
            offsetLeft: 0, //available only for anchors opening the window, or windows set to position: hover
            offsetTop: 0, //""
            iframe: false, //if the window has an href, this will display the href as an iframe instead of requesting the url as an an Ajax.Request
            hover: false, //element object to hover over, or if "true" only available for windows with sourceContainer (an anchor or any element already on the page with an href attribute)
            indicator: false, //element to show or hide when ajax requests, images and iframes are loading
            closeOnClick: false, //does not work with hover,can be: true (click anywhere), 'container' (will refer to this.container), or element (a specific element)
            iframeshim: true, //whether or not to position an iFrameShim underneath the window 
            //effects
            fade: false,
            fadeDuration: 0.75,
            //draggable
            draggable: false,
            onDrag: Prototype.emptyFunction,
            //resizable
            resizable: false,
            minHeight: false,
            minWidth: false,
            maxHeight: false,
            maxWidth: false,
            onResize: Prototype.emptyFunction,
            //draggable and resizable
            constrainToViewport: false,
            //ajax
            method: 'post',
            parameters: {},
            onComplete: Prototype.emptyFunction,
            onSuccess: Prototype.emptyFunction,
            onFailure: Prototype.emptyFunction,
            onException: Prototype.emptyFunction,
            //any element with an href (image,iframe,ajax) will call this after it is done loading
            onRemoteContentLoaded: Prototype.emptyFunction,
            insertRemoteContentAt: false //false will set this to this.container, can be string selector (first returned will be selected), or an Element that must be a child of this.container
        },options || {});
        
        //container setup
        this.indicator = this.options.indicator ? $(this.options.indicator) : false;
        if(container){
            if(typeof(container) == "string" && container.match(Control.Window.uriRegex))
                this.href = container;
            else{
                this.container = $(container);
                //need to create the container now for tooltips (or hover: element with no container already on the page)
                //second call made below will not create the container since the check is done inside createDefaultContainer()
                this.createDefaultContainer(container);
                //if an element with an href was passed in we use it to activate the window
                if(this.container && ((this.container.readAttribute('href') && this.container.readAttribute('href') != '') || (this.options.hover && this.options.hover !== true))){                        
                    if(this.options.hover && this.options.hover !== true)
                        this.sourceContainer = $(this.options.hover);
                    else{
                        this.sourceContainer = this.container;
                        this.href = this.container.readAttribute('href');
                        var rel = this.href.match(/^#(.+)$/);
                        if(rel && rel[1]){
                            this.container = $(rel[1]);
                            this.href = false;
                        }else
                            this.container = false;
                    }
                    //hover or click handling
                    this.sourceContainerOpenHandler = function(event){
                        this.open(event);
                        event.stop();
                        return false;
                    }.bindAsEventListener(this);
                    this.sourceContainerCloseHandler = function(event){
                        this.close(event);
                    }.bindAsEventListener(this);
                    this.sourceContainerMouseMoveHandler = function(event){
                        this.position(event);
                    }.bindAsEventListener(this);
                    if(this.options.hover){
                        this.sourceContainer.observe('mouseenter',this.sourceContainerOpenHandler);
                        this.sourceContainer.observe('mouseleave',this.sourceContainerCloseHandler);
                        if(this.options.position == 'mouse')
                            this.sourceContainer.observe('mousemove',this.sourceContainerMouseMoveHandler);
                    }else
                        this.sourceContainer.observe('click',this.sourceContainerOpenHandler);
                }
            }
        }
        this.createDefaultContainer(container);
        if(this.options.insertRemoteContentAt === false)
            this.options.insertRemoteContentAt = this.container;
        var styles = {
            margin: 0,
            position: 'absolute',
            zIndex: Control.Window.initialZIndexForWindow()
        };
        if(this.options.width)
            styles.width = $value(this.options.width) + 'px';
        if(this.options.height)
            styles.height = $value(this.options.height) + 'px';
        this.container.setStyle(styles);
        if(this.options.className)
            this.container.addClassName(this.options.className);
        this.positionHandler = this.position.bindAsEventListener(this);
        this.outOfBoundsPositionHandler = this.ensureInBounds.bindAsEventListener(this);
        this.bringToFrontHandler = this.bringToFront.bindAsEventListener(this);
        this.container.observe('mousedown',this.bringToFrontHandler);
        this.container.hide();
        this.closeHandler = this.close.bindAsEventListener(this);
        //iframeshim setup
        if(this.options.iframeshim){
            this.iFrameShim = new IframeShim();
            this.iFrameShim.hide();
        }
        //resizable support
        this.applyResizable();
        //draggable support
        this.applyDraggable();
        
        //makes sure the window can't go out of bounds
        Event.observe(window,'resize',this.outOfBoundsPositionHandler);
        
        this.notify('afterInitialize');
    },
    open: function(event){
        if(this.isOpen){
            this.bringToFront();
            return false;
        }
        if(this.notify('beforeOpen') === false)
            return false;
        //closeOnClick
        if(this.options.closeOnClick){
            if(this.options.closeOnClick === true)
                this.closeOnClickContainer = $(document.body);
            else if(this.options.closeOnClick == 'container')
                this.closeOnClickContainer = this.container;
            else if (this.options.closeOnClick == 'overlay'){
                Control.Overlay.load();
                this.closeOnClickContainer = Control.Overlay.container;
            }else
                this.closeOnClickContainer = $(this.options.closeOnClick);
            this.closeOnClickContainer.observe('click',this.closeHandler);
        }
        if(this.href && !this.options.iframe && !this.remoteContentLoaded){
            //link to image
            this.remoteContentLoaded = true;
            if(this.href.match(/\.(jpe?g|gif|png|tiff?)$/i)){
                var img = new Element('img');
                img.observe('load',function(img){
                    this.getRemoteContentInsertionTarget().insert(img);
                    this.position();
                    if(this.notify('onRemoteContentLoaded') !== false){
                        if(this.options.indicator)
                            this.hideIndicator();
                        this.finishOpen();
                    }
                }.bind(this,img));
                img.writeAttribute('src',this.href);
            }else{
                //if this is an ajax window it will only open if the request is successful
                if(!this.ajaxRequest){
                    if(this.options.indicator)
                        this.showIndicator();
                    this.ajaxRequest = new Ajax.Request(this.href,{
                        method: this.options.method,
                        parameters: this.options.parameters,
                        onComplete: function(request){
                            this.notify('onComplete',request);
                            this.ajaxRequest = false;
                        }.bind(this),
                        onSuccess: function(request){
                            this.getRemoteContentInsertionTarget().insert(request.responseText);
                            this.notify('onSuccess',request);
                            if(this.notify('onRemoteContentLoaded') !== false){
                                if(this.options.indicator)
                                    this.hideIndicator();
                                this.finishOpen();
                            }
                        }.bind(this),
                        onFailure: function(request){
                            this.notify('onFailure',request);
                            if(this.options.indicator)
                                this.hideIndicator();
                        }.bind(this),
                        onException: function(request,e){
                            this.notify('onException',request,e);
                            if(this.options.indicator)
                                this.hideIndicator();
                        }.bind(this)
                    });
                }
            }
            return true;
        }else if(this.options.iframe && !this.remoteContentLoaded){
            //iframe
            this.remoteContentLoaded = true;
            if(this.options.indicator)
                this.showIndicator();
            this.getRemoteContentInsertionTarget().insert(Control.Window.iframeTemplate.evaluate({
                href: this.href
            }));
            var iframe = this.container.down('iframe');
            iframe.onload = function(){
                this.notify('onRemoteContentLoaded');
                if(this.options.indicator)
                    this.hideIndicator();
                iframe.onload = null;
            }.bind(this);
        }
        this.finishOpen(event);
        return true
    },
    close: function(event){ //event may or may not be present
        if(!this.isOpen || this.notify('beforeClose',event) === false)
            return false;
        if(this.options.closeOnClick)
            this.closeOnClickContainer.stopObserving('click',this.closeHandler);
        if(this.options.fade){
            this.effects.fade = new Effect.Fade(this.container,{
                queue: {
                    position: 'front',
                    scope: 'Control.Window' + this.numberInSequence
                },
                from: 1,
                to: 0,
                duration: this.options.fadeDuration / 2,
                afterFinish: function(){
                    if(this.iFrameShim)
                        this.iFrameShim.hide();
                    this.isOpen = false;
                    this.notify('afterClose');
                }.bind(this)
            });
        }else{
            this.container.hide();
            if(this.iFrameShim)
                this.iFrameShim.hide();
        }
        if(this.ajaxRequest)
            this.ajaxRequest.transport.abort();
        if(!(this.options.draggable || this.options.resizable) && this.options.position == 'center')
            Event.stopObserving(window,'resize',this.positionHandler);
        if(!this.options.draggable && this.options.position == 'center')
            Event.stopObserving(window,'scroll',this.positionHandler);
        if(this.options.indicator)
            this.hideIndicator();
        if(!this.options.fade){
            this.isOpen = false;
            this.notify('afterClose');
        }
        return true;
    },
    position: function(event){
        if(this.options.position == 'mouse'){
            var xy = [Event.pointerX(event), Event.pointerY(event)];
            this.container.setStyle({
                top: xy[1] + $value(this.options.offsetTop) + 'px',
                left: xy[0] + $value(this.options.offsetLeft) + 'px'
            });
            return;
        }
        var container_dimensions = this.container.getDimensions();
        var viewport_dimensions = document.viewport.getDimensions();
        Position.prepare();
        var offset_left = (Position.deltaX + Math.floor((viewport_dimensions.width - container_dimensions.width) / 2));
        var offset_top = (Position.deltaY + ((viewport_dimensions.height > container_dimensions.height) ? Math.floor((viewport_dimensions.height - container_dimensions.height) / 2) : 0));
        if(this.options.position == 'center'){
            this.container.setStyle({
                top: (container_dimensions.height <= viewport_dimensions.height) ? ((offset_top != null && offset_top > 0) ? offset_top : 0) + 'px' : 0,
                left: (container_dimensions.width <= viewport_dimensions.width) ? ((offset_left != null && offset_left > 0) ? offset_left : 0) + 'px' : 0
            });
        }else if(this.options.position == 'relative'){
            var xy = this.sourceContainer.cumulativeOffset();
            var top = xy[1] + $value(this.options.offsetTop);
            var left = xy[0] + $value(this.options.offsetLeft);
            this.container.setStyle({
                top: (container_dimensions.height <= viewport_dimensions.height) ? (this.options.constrainToViewport ? Math.max(0,Math.min(viewport_dimensions.height - (container_dimensions.height),top)) : top) + 'px' : 0,
                left: (container_dimensions.width <= viewport_dimensions.width) ? (this.options.constrainToViewport ? Math.max(0,Math.min(viewport_dimensions.width - (container_dimensions.width),left)) : left) + 'px' : 0
            });
        }else if(this.options.position.length){
            var top = $value(this.options.position[1]) + $value(this.options.offsetTop);
            var left = $value(this.options.position[0]) + $value(this.options.offsetLeft);
            this.container.setStyle({
                top: (container_dimensions.height <= viewport_dimensions.height) ? (this.options.constrainToViewport ? Math.max(0,Math.min(viewport_dimensions.height - (container_dimensions.height),top)) : top) + 'px' : 0,
                left: (container_dimensions.width <= viewport_dimensions.width) ? (this.options.constrainToViewport ? Math.max(0,Math.min(viewport_dimensions.width - (container_dimensions.width),left)) : left) + 'px' : 0
            });
        }
        if(this.iFrameShim)
            this.updateIFrameShimZIndex();
    },
    ensureInBounds: function(){
        if(!this.isOpen)
            return;
        var viewport_dimensions = document.viewport.getDimensions();
        var container_offset = this.container.cumulativeOffset();
        var container_dimensions = this.container.getDimensions();
        if(container_offset.left + container_dimensions.width > viewport_dimensions.width){
            this.container.setStyle({
                left: (Math.max(0,viewport_dimensions.width - container_dimensions.width)) + 'px'
            });
        }
        if(container_offset.top + container_dimensions.height > viewport_dimensions.height){
            this.container.setStyle({
                top: (Math.max(0,viewport_dimensions.height - container_dimensions.height)) + 'px'
            });
        }
    },
    bringToFront: function(){
        Control.Window.bringToFront(this);
        this.notify('bringToFront');
    },
    destroy: function(){
        this.container.stopObserving('mousedown',this.bringToFrontHandler);
        if(this.draggable){
            Draggables.removeObserver(this.container);
            this.draggable.handle.stopObserving('mousedown',this.bringToFrontHandler);
            this.draggable.destroy();
        }
        if(this.resizable){
            Resizables.removeObserver(this.container);
            this.resizable.handle.stopObserving('mousedown',this.bringToFrontHandler);
            this.resizable.destroy();
        }
        if(this.container && !this.sourceContainer)
            this.container.remove();
        if(this.sourceContainer){
            if(this.options.hover){
                this.sourceContainer.stopObserving('mouseenter',this.sourceContainerOpenHandler);
                this.sourceContainer.stopObserving('mouseleave',this.sourceContainerCloseHandler);
                if(this.options.position == 'mouse')
                    this.sourceContainer.stopObserving('mousemove',this.sourceContainerMouseMoveHandler);
            }else
                this.sourceContainer.stopObserving('click',this.sourceContainerOpenHandler);
        }
        if(this.iFrameShim)
            this.iFrameShim.destroy();
        Event.stopObserving(window,'resize',this.outOfBoundsPositionHandler);
        Control.Window.windows = Control.Window.windows.without(this);
        this.notify('afterDestroy');
    },
    //private
    applyResizable: function(){
        if(this.options.resizable){
            if(typeof(Resizable) == "undefined")
                throw "Control.Window requires resizable.js to be loaded.";
            var resizable_handle = null;
            if(this.options.resizable === true){
                resizable_handle = new Element('div',{
                    className: 'resizable_handle'
                });
                this.container.insert(resizable_handle);
            }else
                resizable_handle = $(this.options.resziable);
            this.resizable = new Resizable(this.container,{
                handle: resizable_handle,
                minHeight: this.options.minHeight,
                minWidth: this.options.minWidth,
                maxHeight: this.options.constrainToViewport ? function(element){
                    //viewport height - top - total border height
                    return (document.viewport.getDimensions().height - parseInt(element.style.top || 0)) - (element.getHeight() - parseInt(element.style.height || 0));
                } : this.options.maxHeight,
                maxWidth: this.options.constrainToViewport ? function(element){
                    //viewport width - left - total border width
                    return (document.viewport.getDimensions().width - parseInt(element.style.left || 0)) - (element.getWidth() - parseInt(element.style.width || 0));
                } : this.options.maxWidth
            });
            this.resizable.handle.observe('mousedown',this.bringToFrontHandler);
            Resizables.addObserver(new Control.Window.LayoutUpdateObserver(this,function(){
                if(this.iFrameShim)
                    this.updateIFrameShimZIndex();
                this.notify('onResize');
            }.bind(this)));
        }
    },
    applyDraggable: function(){
        if(this.options.draggable){
            if(typeof(Draggables) == "undefined")
                throw "Control.Window requires dragdrop.js to be loaded.";
            var draggable_handle = null;
            if(this.options.draggable === true){
                draggable_handle = new Element('div',{
                    className: 'draggable_handle'
                });
                this.container.insert(draggable_handle);
            }else
                draggable_handle = $(this.options.draggable);
            this.draggable = new Draggable(this.container,{
                handle: draggable_handle,
                constrainToViewport: this.options.constrainToViewport,
                zindex: this.container.getStyle('z-index'),
                starteffect: function(){
                    if(Prototype.Browser.IE){
                        this.old_onselectstart = document.onselectstart;
                        document.onselectstart = function(){
                            return false;
                        };
                    }
                }.bind(this),
                endeffect: function(){
                    document.onselectstart = this.old_onselectstart;
                }.bind(this)
            });
            this.draggable.handle.observe('mousedown',this.bringToFrontHandler);
            Draggables.addObserver(new Control.Window.LayoutUpdateObserver(this,function(){
                if(this.iFrameShim)
                    this.updateIFrameShimZIndex();
                this.notify('onDrag');
            }.bind(this)));
        }
    },
    createDefaultContainer: function(container){
        if(!this.container){
            //no container passed or found, create it
            this.container = new Element('div',{
                id: 'control_window_' + this.numberInSequence
            });
            $(document.body).insert(this.container);
            if(typeof(container) == "string" && $(container) == null && !container.match(/^#(.+)$/) && !container.match(Control.Window.uriRegex))
                this.container.update(container);
        }
    },
    finishOpen: function(event){
        this.bringToFront();
        if(this.options.fade){
            if(typeof(Effect) == "undefined")
                throw "Control.Window requires effects.js to be loaded.";
            if(this.effects.fade)
                this.effects.fade.cancel();
            this.effects.appear = new Effect.Appear(this.container,{
                queue: {
                    position: 'end',
                    scope: 'Control.Window.' + this.numberInSequence
                },
                from: 0,
                to: 1,
                duration: this.options.fadeDuration / 2,
                afterFinish: function(){
                    if(this.iFrameShim)
                        this.updateIFrameShimZIndex();
                    this.isOpen = true;
                    this.notify('afterOpen');
                }.bind(this)
            });
        }else
            this.container.show();
        this.position(event);
        if(!(this.options.draggable || this.options.resizable) && this.options.position == 'center')
            Event.observe(window,'resize',this.positionHandler,false);
        if(!this.options.draggable && this.options.position == 'center')
            Event.observe(window,'scroll',this.positionHandler,false);
        if(!this.options.fade){
            this.isOpen = true;
            this.notify('afterOpen');
        }
        return true;
    },
    showIndicator: function(){
        this.showIndicatorTimeout = window.setTimeout(function(){
            if(this.options.fade){
                this.indicatorEffects.appear = new Effect.Appear(this.indicator,{
                    queue: {
                        position: 'front',
                        scope: 'Control.Window.indicator.' + this.numberInSequence
                    },
                    from: 0,
                    to: 1,
                    duration: this.options.fadeDuration / 2
                });
            }else
                this.indicator.show();
        }.bind(this),Control.Window.indicatorTimeout);
    },
    hideIndicator: function(){
        if(this.showIndicatorTimeout)
            window.clearTimeout(this.showIndicatorTimeout);
        this.indicator.hide();
    },
    getRemoteContentInsertionTarget: function(){
        return typeof(this.options.insertRemoteContentAt) == "string" ? this.container.down(this.options.insertRemoteContentAt) : $(this.options.insertRemoteContentAt);
    },
    updateIFrameShimZIndex: function(){
        if(this.iFrameShim)
            this.iFrameShim.positionUnder(this.container);
    }
});
//class methods
Object.extend(Control.Window,{
    windows: [],
    baseZIndex: 9999,
    indicatorTimeout: 250,
    iframeTemplate: new Template('<iframe src="#{href}" width="100%" height="100%" frameborder="0"></iframe>'),
    uriRegex: /^(\/|\#|https?\:\/\/|[\w]+\/)/,
    bringToFront: function(w){
        Control.Window.windows = Control.Window.windows.without(w);
        Control.Window.windows.push(w);
        Control.Window.windows.each(function(w,i){
            var z_index = Control.Window.baseZIndex + i;
            w.container.setStyle({
                zIndex: z_index
            });
            if(w.isOpen){
                if(w.iFrameShim)
                w.updateIFrameShimZIndex();
            }
            if(w.options.draggable)
                w.draggable.options.zindex = z_index;
        });
    },
    open: function(container,options){
        var w = new Control.Window(container,options);
        w.open();
        return w;
    },
    //protected
    initialZIndexForWindow: function(w){
        return Control.Window.baseZIndex + (Control.Window.windows.length - 1);
    }
});
Object.Event.extend(Control.Window);

//this is the observer for both Resizables and Draggables
Control.Window.LayoutUpdateObserver = Class.create({
    initialize: function(w,observer){
        this.w = w;
        this.element = $(w.container);
        this.observer = observer;
    },
    onStart: Prototype.emptyFunction,
    onEnd: function(event_name,instance){
        if(instance.element == this.element && this.iFrameShim)
            this.w.updateIFrameShimZIndex();
    },
    onResize: function(event_name,instance){
        if(instance.element == this.element)
            this.observer(this.element);
    },
    onDrag: function(event_name,instance){
        if(instance.element == this.element)
            this.observer(this.element);
    }
});

//overlay for Control.Modal
Control.Overlay = {
    id: 'control_overlay',
    loaded: false,
    container: false,
    lastOpacity: 0,
    styles: {
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        zIndex: 9998
    },
    ieStyles: {
        position: 'absolute',
        top: 0,
        left: 0,
        zIndex: 9998
    },
    effects: {
        fade: false,
        appear: false
    },
    load: function(){
        if(Control.Overlay.loaded)
            return false;
        Control.Overlay.loaded = true;
        Control.Overlay.container = new Element('div',{
            id: Control.Overlay.id
        });
        $(document.body).insert(Control.Overlay.container);
        if(Prototype.Browser.IE){
            Control.Overlay.container.setStyle(Control.Overlay.ieStyles);
            Event.observe(window,'scroll',Control.Overlay.positionOverlay);
            Event.observe(window,'resize',Control.Overlay.positionOverlay);
            Control.Overlay.observe('beforeShow',Control.Overlay.positionOverlay);
        }else
            Control.Overlay.container.setStyle(Control.Overlay.styles);
        Control.Overlay.iFrameShim = new IframeShim();
        Control.Overlay.iFrameShim.hide();
        Event.observe(window,'resize',Control.Overlay.positionIFrameShim);
        Control.Overlay.container.hide();
        return true;
    },
    unload: function(){
        if(!Control.Overlay.loaded)
            return false;
        Event.stopObserving(window,'resize',Control.Overlay.positionOverlay);
        Control.Overlay.stopObserving('beforeShow',Control.Overlay.positionOverlay);
        Event.stopObserving(window,'resize',Control.Overlay.positionIFrameShim);
        Control.Overlay.iFrameShim.destroy();
        Control.Overlay.container.remove();
        Control.Overlay.loaded = false;
        return true;
    },
    show: function(opacity,fade){
        if(Control.Overlay.notify('beforeShow') === false)
            return false;
        Control.Overlay.lastOpacity = opacity;
        Control.Overlay.positionIFrameShim();
        Control.Overlay.iFrameShim.show();
        if(fade){
            if(typeof(Effect) == "undefined")
                throw "Control.Window requires effects.js to be loaded.";
            if(Control.Overlay.effects.fade)
                Control.Overlay.effects.fade.cancel();
            Control.Overlay.effects.appear = new Effect.Appear(Control.Overlay.container,{
                queue: {
                    position: 'end',
                    scope: 'Control.Overlay'
                },
                afterFinish: function(){
                    Control.Overlay.notify('afterShow');
                },
                from: 0,
                to: Control.Overlay.lastOpacity,
                duration: (fade === true ? 0.75 : fade) / 2
            });
        }else{
            Control.Overlay.container.setStyle({
                opacity: opacity || 1
            });
            Control.Overlay.container.show();
            Control.Overlay.notify('afterShow');
        }
        return true;
    },
    hide: function(fade){
        if(Control.Overlay.notify('beforeHide') === false)
            return false;
        if(Control.Overlay.effects.appear)
            Control.Overlay.effects.appear.cancel();
        Control.Overlay.iFrameShim.hide();
        if(fade){
            Control.Overlay.effects.fade = new Effect.Fade(Control.Overlay.container,{
                queue: {
                    position: 'front',
                    scope: 'Control.Overlay'
                },
                afterFinish: function(){
                    Control.Overlay.notify('afterHide');
                },
                from: Control.Overlay.lastOpacity,
                to: 0,
                duration: (fade === true ? 0.75 : fade) / 2
            });
        }else{
            Control.Overlay.container.hide();
            Control.Overlay.notify('afterHide');
        }
        return true;
    },
    positionIFrameShim: function(){
        if(Control.Overlay.container.visible())
            Control.Overlay.iFrameShim.positionUnder(Control.Overlay.container);
    },
    //IE only
    positionOverlay: function(){
        Control.Overlay.container.setStyle({
            width: document.body.clientWidth + 'px',
            height: document.body.clientHeight + 'px'
        });
    }
};
Object.Event.extend(Control.Overlay);

Control.ToolTip = Class.create(Control.Window,{
    initialize: function($super,container,tooltip,options){
        $super(tooltip,Object.extend(Object.extend(Object.clone(Control.ToolTip.defaultOptions),options || {}),{
            position: 'front',
            hover: container
        }));
    }
});
Object.extend(Control.ToolTip,{
    defaultOptions: {
        offsetLeft: 10
    }
});

Control.Modal = Class.create(Control.Window,{
    initialize: function($super,container,options){
        Control.Modal.InstanceMethods.beforeInitialize.bind(this)();
        $super(container,Object.extend(Object.clone(Control.Modal.defaultOptions),options || {}));
    }
});
Object.extend(Control.Modal,{
    defaultOptions: {
        overlayOpacity: 0.5,
        closeOnClick: 'overlay'
    },
    current: false,
    open: function(container,options){
        var modal = new Control.Modal(container,options);
        modal.open();
        return modal;
    },
    close: function(){
        if(Control.Modal.current)
            Control.Modal.current.close();
    },
    InstanceMethods: {
        beforeInitialize: function(){
            Control.Overlay.load();
            this.overlayFinishedOpening = false;
            this.observe('beforeOpen',Control.Modal.Observers.beforeOpen.bind(this));
            this.observe('afterOpen',Control.Modal.Observers.afterOpen.bind(this));
            this.observe('afterClose',Control.Modal.Observers.afterClose.bind(this));
        }
    },
    Observers: {
        beforeOpen: function(){
            if(!this.overlayFinishedOpening){
                Control.Overlay.observeOnce('afterShow',function(){
                    this.overlayFinishedOpening = true;
                    this.open();
                }.bind(this));
                Control.Overlay.show(this.options.overlayOpacity,this.options.fade ? this.options.fadeDuration : false);
                throw $break;
            }else
            Control.Window.windows.without(this).invoke('close');
        },
        afterOpen: function(){
            Control.Modal.current = this;
        },
        afterClose: function(){
            Control.Overlay.hide(this.options.fade ? this.options.fadeDuration : false);
            Control.Modal.current = false;
            this.overlayFinishedOpening = false;
        }
    }
});

Control.LightBox = Class.create(Control.Window,{
    initialize: function($super,container,options){
        this.allImagesLoaded = false;
        if(options.modal){
            var options = Object.extend(Object.clone(Control.LightBox.defaultOptions),options || {});
            options = Object.extend(Object.clone(Control.Modal.defaultOptions),options);
            options = Control.Modal.InstanceMethods.beforeInitialize.bind(this)(options);
            $super(container,options);
        }else
            $super(container,Object.extend(Object.clone(Control.LightBox.defaultOptions),options || {}));
        this.hasRemoteContent = this.href && !this.options.iframe;
        if(this.hasRemoteContent)
            this.observe('onRemoteContentLoaded',Control.LightBox.Observers.onRemoteContentLoaded.bind(this));
        else
            this.applyImageObservers();
        this.observe('beforeOpen',Control.LightBox.Observers.beforeOpen.bind(this));
    },
    applyImageObservers:function(){
        var images = this.getImages();
        this.numberImagesToLoad = images.length;
        this.numberofImagesLoaded = 0;
        images.each(function(image){
            image.observe('load',function(image){
                ++this.numberofImagesLoaded;
                if(this.numberImagesToLoad == this.numberofImagesLoaded){
                    this.allImagesLoaded = true;
                    this.onAllImagesLoaded();
                }
            }.bind(this,image));
            image.hide();
        }.bind(this));
    },
    onAllImagesLoaded: function(){
        this.getImages().each(function(image){
            this.showImage(image);
        }.bind(this));
        if(this.hasRemoteContent){
            if(this.options.indicator)
                this.hideIndicator();
            this.finishOpen();
        }else
            this.open();
    },
    getImages: function(){
        return this.container.select(Control.LightBox.imageSelector);
    },
    showImage: function(image){
        image.show();
    }
});
Object.extend(Control.LightBox,{
    imageSelector: 'img',
    defaultOptions: {},
    Observers: {
        beforeOpen: function(){
            if(!this.hasRemoteContent && !this.allImagesLoaded)
                throw $break;
        },
        onRemoteContentLoaded: function(){
            this.applyImageObservers();
            if(!this.allImagesLoaded)
                throw $break;
        }
    }
});
//------------------------------------------------------------------------------------------------//
// Script:carousel.js
//------------------------------------------------------------------------------------------------//
/*
Copyright (c) 2009 Victor Stanciu - http://www.victorstanciu.ro

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/
Carousel = Class.create(Abstract, {
	initialize: function (scroller, slides, controls, options) {
		this.scrolling	= false;
		this.scroller	= $(scroller);
		this.slides		= slides;
		this.controls	= controls;

		this.options    = Object.extend({
            duration:           1,
            auto:               false,
            frequency:          3,
            visibleSlides:      1,
            controlClassName:   'slider-control',
            jumperClassName:    'slider-jumper',
            disabledClassName:  'slider-disabled',
            selectedClassName:  'slider-selected',
            circular:           false,
            wheel:              true,
            effect:             'scroll',
            transition:         'sinoidal'
        }, options || {});
        
        if (this.options.effect == 'fade') {
            this.options.circular = true;
        }

		this.slides.each(function(slide, index) {
			slide._index = index;
        });

		if (this.controls) {
            this.controls.invoke('observe', 'click', this.click.bind(this));
        }
        
        if (this.options.wheel) {            
            this.scroller.observe('mousewheel', this.wheel.bindAsEventListener(this)).observe('DOMMouseScroll', this.wheel.bindAsEventListener(this));;
        }

        if (this.options.auto) {
            this.start();
        }

		if (this.options.initial) {
			var initialIndex = this.slides.indexOf($(this.options.initial));
			if (initialIndex > (this.options.visibleSlides - 1) && this.options.visibleSlides > 1) {               
				if (initialIndex > this.slides.length - (this.options.visibleSlides + 1)) {
					initialIndex = this.slides.length - this.options.visibleSlides;
				}
			}
            this.moveTo(this.slides[initialIndex]);
		}
	},

	click: function (event) {
		this.stop();
		var element = event.findElement('a');
		if (!element.hasClassName(this.options.disabledClassName)) {
			if (element.hasClassName(this.options.controlClassName)) {
				eval("this." + element.rel + "()");
            } else if (element.hasClassName(this.options.jumperClassName)) {
                this.moveTo(element.rel);
                if (this.options.selectedClassName) {
                    this.controls.invoke('removeClassName', this.options.selectedClassName);
                    element.addClassName(this.options.selectedClassName);
                }
            }
        }
		this.deactivateControls();
		event.stop();
    },

	moveTo: function (element) {
		if (this.options.beforeMove && (typeof this.options.beforeMove == 'function')) {
			this.options.beforeMove();
        }

		this.previous = this.current ? this.current : this.slides[0];
		this.current  = $(element);

		var scrollerOffset = this.scroller.cumulativeOffset();
		var elementOffset  = this.current.cumulativeOffset();

		if (this.scrolling) {
			this.scrolling.cancel();
		}

        switch (this.options.effect) {
            case 'fade':               
                this.scrolling = new Effect.Opacity(this.scroller, {
                    from:   1.0,
                    to:     0,
                    duration: this.options.duration,
                    afterFinish: (function () {
                        this.scroller.scrollLeft = elementOffset[0] - scrollerOffset[0];
                        this.scroller.scrollTop  = elementOffset[1] - scrollerOffset[1];

                        new Effect.Opacity(this.scroller, {
                            from: 0,
                            to: 1.0,
                            duration: this.options.duration,
                            afterFinish: (function () {
                                if (this.controls) {
                                    this.activateControls();
                                }
                                if (this.options.afterMove && (typeof this.options.afterMove == 'function')) {
                                    this.options.afterMove();
                                }
                            }).bind(this)
                        });
                    }
                ).bind(this)});
            break;
            case 'scroll':
            default:
                var transition;
                switch (this.options.transition) {
                    case 'spring':
                        transition = Effect.Transitions.spring;
                        break;
                    case 'sinoidal':
                    default:
                        transition = Effect.Transitions.sinoidal;
                        break;
                }

                this.scrolling = new Effect.SmoothScroll(this.scroller, {
                    duration: this.options.duration,
                    x: (elementOffset[0] - scrollerOffset[0]),
                    y: (elementOffset[1] - scrollerOffset[1]),
                    transition: transition,
                    afterFinish: (function () {
                        if (this.controls) {
                            this.activateControls();
                        }
                        if (this.options.afterMove && (typeof this.options.afterMove == 'function')) {
                            this.options.afterMove();
                        }                        
                        this.scrolling = false;
                    }).bind(this)});
            break;
        }

		return false;
	},

	prev: function () {
		var prevIndex = 0;
		if (this.current) {
			var currentIndex = this.current._index;
			prevIndex = (currentIndex == 0) ? (this.options.circular ? this.slides.length - 1 : 0) : currentIndex - 1;
        } else {
            prevIndex = (this.options.circular ? this.slides.length - 1 : 0);
        }

		if (prevIndex == (this.slides.length - 1) && this.options.circular && this.options.effect != 'fade') {
			this.scroller.scrollLeft =  (this.slides.length - 1) * this.slides.first().getWidth();
			this.scroller.scrollTop =  (this.slides.length - 1) * this.slides.first().getHeight();
			prevIndex = this.slides.length - 2;
        }

		this.moveTo(this.slides[prevIndex]);
	},

	next: function () {
		var nextIndex = 0;
		if (this.current) {
			var currentIndex = this.current._index;
			nextIndex = (this.slides.length - 1 == currentIndex) ? (this.options.circular ? 0 : currentIndex) : currentIndex + 1;
        } else {
            nextIndex = 1;
        }

		if (nextIndex == 0 && this.options.circular && this.options.effect != 'fade') {
			this.scroller.scrollLeft = 0;
			this.scroller.scrollTop  = 0;
			nextIndex = 1;
        }

		if (nextIndex > this.slides.length - (this.options.visibleSlides + 1)) {
			nextIndex = this.slides.length - this.options.visibleSlides;
		}		

		this.moveTo(this.slides[nextIndex]);
	},

	first: function () {
		this.moveTo(this.slides[0]);
    },

	last: function () {
		this.moveTo(this.slides[this.slides.length - 1]);
    },

	toggle: function () {
		if (this.previous) {
			this.moveTo(this.slides[this.previous._index]);
        } else {
            return false;
        }
    },

	stop: function () {
		if (this.timer) {
			clearTimeout(this.timer);
		}
	},

	start: function () { 
        this.periodicallyUpdate();
    },

	pause: function () {
		this.stop();
		this.activateControls();
    },

	resume: function (event) {
		if (event) {
			var related = event.relatedTarget || event.toElement;
			if (!related || (!this.slides.include(related) && !this.slides.any(function (slide) { return related.descendantOf(slide); }))) {
				this.start();
            }
        } else {
            this.start();
        }
    },

	periodicallyUpdate: function () {
		if (this.timer != null) {
			clearTimeout(this.timer);
			this.next();
        }
		this.timer = setTimeout(this.periodicallyUpdate.bind(this), this.options.frequency * 1000);
    },
    
    wheel: function (event) {
        event.cancelBubble = true;
        event.stop();
        
		var delta = 0;
		if (!event) {
            event = window.event;
        }
		if (event.wheelDelta) {
			delta = event.wheelDelta / 120; 
		} else if (event.detail) { 
            delta = -event.detail / 3;	
        }        
       
        if (!this.scrolling) {
            this.deactivateControls();
            if (delta > 0) {
                this.prev();
            } else {
                this.next();
            }            
        }
        
		return Math.round(delta); //Safari Round
    },

	deactivateControls: function () {
		this.controls.invoke('addClassName', this.options.disabledClassName);
    },

	activateControls: function () {
		this.controls.invoke('removeClassName', this.options.disabledClassName);
    }
});


Effect.SmoothScroll = Class.create();
Object.extend(Object.extend(Effect.SmoothScroll.prototype, Effect.Base.prototype), {
	initialize: function (element) {
		this.element = $(element);
		var options = Object.extend({ x: 0, y: 0, mode: 'absolute' } , arguments[1] || {});
		this.start(options);
    },

	setup: function () {
		if (this.options.continuous && !this.element._ext) {
			this.element.cleanWhitespace();
			this.element._ext = true;
			this.element.appendChild(this.element.firstChild);
        }

		this.originalLeft = this.element.scrollLeft;
		this.originalTop  = this.element.scrollTop;

		if (this.options.mode == 'absolute') {
			this.options.x -= this.originalLeft;
			this.options.y -= this.originalTop;
        }
    },

	update: function (position) {
		this.element.scrollLeft = this.options.x * position + this.originalLeft;
		this.element.scrollTop  = this.options.y * position + this.originalTop;
    }
});
//------------------------------------------------------------------------------------------------//