package uk.recurse.bitwrapper;

import static com.google.common.base.Preconditions.checkArgument;

class ProxyFactory {

    public <T> T create(MethodHandler handler, Class<T> view) {
        checkArgument(view.isInterface(), view + " is not an interface");
        javassist.util.proxy.ProxyFactory f = new javassist.util.proxy.ProxyFactory();
        f.setInterfaces(new Class<?>[]{view});
        f.setFilter(m -> !m.isDefault() && !(m.getName().equals("finalize") && m.getParameterCount() == 0));
        javassist.util.proxy.MethodHandler mh = (proxy, method, proceed, args) -> handler.invoke(proxy, method);
        try {
            Object proxy = f.create(new Class<?>[0], new Object[0], mh);
            return view.cast(proxy);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
