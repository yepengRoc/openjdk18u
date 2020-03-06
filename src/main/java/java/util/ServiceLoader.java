package java.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * <p>一个简单的服务提供商加载工具。
 * <p>服务是一组著名的接口和（通常是抽象的）类。服务提供者是服务的特定实现。
 * 提供程序中的类通常实现接口，并子类化服务本身中定义的类。服务提供程序可以扩展的形式安装在Java平台的实现中，
 * 也就是说，将jar文件放置在任何常用扩展目录中。也可以通过将提供者添加到应用程序的类路径或通过其他一些特定于平台的方式来使提供者可用。
 *
 * <p>出于加载的目的，服务由单一类型表示，即单一接口或抽象类。（可以使用一个具体的类，但是不建议这样做。）
 * 给定服务的提供者包含一个或多个具体类，这些具体类使用该提供者特定的数据和代码来扩展此服务类型。提供者类通常不是整个提供者本身，
 * 而是包含足够信息以决定提供者是否能够满足特定请求以及可以按需创建实际提供者的代码的代理。提供程序类的细节往往是高度特定于服务的；
 * 没有单个类或接口可能会统一它们，因此此处未定义此类。此功能强制执行的唯一要求是，提供程序类必须具有零参数构造函数，以便可以在加载期间实例化它们。
 *
 * <p>通过将提供者配置文件放置在资源目录META-INF / services中来标识服务提供者。文件名是服务类型的标准二进制名称。
 * 该文件包含一个具体的提供程序类的标准二进制名称列表，每行一个。每个名称周围的空格和制表符以及空白行将被忽略。
 * 注释字符为“＃”（“ \ u0023”，数字符号）；在每一行中，第一个注释字符之后的所有字符都将被忽略。该文件必须使用UTF-8编码。
 *
 * <p>如果一个特定的具体提供程序类在多个配置文件中被命名，或者在同一配置文件中被多次命名，则重复项将被忽略。
 * 命名特定提供程序的配置文件不必与提供程序本身位于同一jar文件或其他分发单元中。
 * 该提供程序必须可以从最初查询定位配置文件的同一类加载程序进行访问；请注意，这不一定是实际从中加载文件的类加载器。
 *
 * <p>实现类的定位和懒实例化，即按需实例化。服务加载器维护到目前为止已加载的提供者的缓存。
 * 每次迭代器方法的调用都会返回一个迭代器，该迭代器首先按实例化顺序生成高速缓存的所有元素，
 * 然后懒惰地定位和实例化任何剩余的提供程序，依次将每个提供程序添加到高速缓存中。可以通过reload方法清除缓存。
 *
 * <p>服务加载程序始终在调用方的安全上下文中执行。受信任的系统代码通常应从特权安全上下文中调用此类中的方法以及它们返回的迭代器的方法。
 *
 * <p>此类的实例不适用于多个并发线程。
 *
 * <p>除非另有说明，否则将null参数传递给此类中的任何方法都将引发NullPointerException。
 *
 * <p> Unless otherwise specified, passing a <tt>null</tt> argument to any
 * method in this class will cause a {@link NullPointerException} to be thrown.
 *
 *
 * <p><span style="font-weight: bold; padding-right: 1em">Example</span>
 * 假设我们有一个服务类型com.example.CodecSet，它旨在表示某种协议的编码器/解码器对的集合。
 * 在这种情况下，它是一个具有两个抽象方法的抽象类：
 *
 * <blockquote><pre>
 * public abstract Encoder getEncoder(String encodingName);
 * public abstract Decoder getDecoder(String encodingName);</pre></blockquote>
 *
 *每个方法都返回一个适当的对象，如果提供者不支持给定的编码，则返回null。典型的提供程序支持多种编码。
 *
 * 如果com.example.impl.StandardCodecs是CodecSet服务的实现，则其jar文件还包含一个名为
 * META-INF/services/com.example.CodecSet
 *
 * <p> 此文件包含单行：
 * com.example.impl.StandardCodecs    # Standard codecs</pre></blockquote>
 *
 * <p> CodecSet类在初始化时创建并保存一个服务实例：
 *  private static ServiceLoader<CodecSet> codecSetLoader
 *      = ServiceLoader.load(CodecSet.class);
 *
 * <p> 为了找到给定编码名称的编码器，它定义了一个静态工厂方法，
 * 该方法迭代已知和可用的提供者，仅在找到合适的编码器或用尽提供者时返回。
 *
 * <blockquote><pre>
 * public static Encoder getEncoder(String encodingName) {
 *     for (CodecSet cp : codecSetLoader) {
 *         Encoder enc = cp.getEncoder(encodingName);
 *         if (enc != null)
 *             return enc;
 *     }
 *     return null;
 * }</pre></blockquote>
 *
 * <p> 类似地定义了getDecoder方法。
 *
 * <p>使用说明如果用于提供程序加载的类加载程序的类路径包括远程网络URL，则将在搜索提供程序配置文件的过程中取消引用这些URL。
 *
 * <p>此活动是正常的，尽管它可能会导致在Web服务器日志中创建令人费解的条目。
 * 但是，如果未正确配置Web服务器，则此活动可能导致提供商加载算法错误地失败。
 *
 * <p>当请求的资源不存在时，
 * Web服务器应返回HTTP 404（未找到）响应。但是，在某些情况下，有时会错误地将Web服务器配置为返回HTTP 200（OK）
 * 响应以及有用的HTML错误页面。当此类尝试将HTML页面解析为提供程序配置文件时，这将引发ServiceConfigurationError。
 * 解决此问题的最佳方法是修复配置错误的Web服务器，以返回正确的响应代码（HTTP 404）和HTML错误页面。
 *
 * @param  <S>
 *         The type of the service to be loaded by this loader
 *
 * @author Mark Reinhold
 * @since 1.6
 */

public final class ServiceLoader<S>
    implements Iterable<S>
{

    private static final String PREFIX = "META-INF/services/";

    // The class or interface representing the service being loaded
    private final Class<S> service;

    // The class loader used to locate, load, and instantiate providers
    private final ClassLoader loader;

    // The access control context taken when the ServiceLoader is created
    private final AccessControlContext acc;

    // Cached providers, in instantiation order
    private LinkedHashMap<String,S> providers = new LinkedHashMap<>();

    // The current lazy-lookup iterator
    private LazyIterator lookupIterator;

    /**
     * Clear this loader's provider cache so that all providers will be
     * reloaded.
     *
     * <p> After invoking this method, subsequent invocations of the {@link
     * #iterator() iterator} method will lazily look up and instantiate
     * providers from scratch, just as is done by a newly-created loader.
     *
     * <p> This method is intended for use in situations in which new providers
     * can be installed into a running Java virtual machine.
     */
    public void reload() {
        providers.clear();
        lookupIterator = new LazyIterator(service, loader);
    }

    private ServiceLoader(Class<S> svc, ClassLoader cl) {
        service = Objects.requireNonNull(svc, "Service interface cannot be null");
        loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
        acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
        reload();
    }

    private static void fail(Class<?> service, String msg, Throwable cause)
        throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
                                            cause);
    }

    private static void fail(Class<?> service, String msg)
        throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class<?> service, URL u, int line, String msg)
        throws ServiceConfigurationError
    {
        fail(service, u + ":" + line + ": " + msg);
    }

    // Parse a single line from the given configuration file, adding the name
    // on the line to the names list.
    //
    private int parseLine(Class<?> service, URL u, BufferedReader r, int lc,
                          List<String> names)
        throws IOException, ServiceConfigurationError
    {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                fail(service, u, lc, "Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            if (!providers.containsKey(ln) && !names.contains(ln))
                names.add(ln);
        }
        return lc + 1;
    }

    // Parse the content of the given URL as a provider-configuration file.
    //
    // @param  service
    //         The service type for which providers are being sought;
    //         used to construct error detail strings
    //
    // @param  u
    //         The URL naming the configuration file to be parsed
    //
    // @return A (possibly empty) iterator that will yield the provider-class
    //         names in the given configuration file that are not yet members
    //         of the returned set
    //
    // @throws ServiceConfigurationError
    //         If an I/O error occurs while reading from the given URL, or
    //         if a configuration-file format error is detected
    //
    private Iterator<String> parse(Class<?> service, URL u)
        throws ServiceConfigurationError
    {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names)) >= 0);
        } catch (IOException x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    // Private inner class implementing fully-lazy provider lookup
    //
    private class LazyIterator
        implements Iterator<S>
    {

        Class<S> service;
        ClassLoader loader;
        Enumeration<URL> configs = null;
        Iterator<String> pending = null;
        String nextName = null;

        private LazyIterator(Class<S> service, ClassLoader loader) {
            this.service = service;
            this.loader = loader;
        }

        private boolean hasNextService() {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + service.getName();
                    if (loader == null)
                        configs = ClassLoader.getSystemResources(fullName);
                    else
                        configs = loader.getResources(fullName);
                } catch (IOException x) {
                    fail(service, "Error locating configuration files", x);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(service, configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }

        private S nextService() {
            if (!hasNextService())
                throw new NoSuchElementException();
            String cn = nextName;
            nextName = null;
            Class<?> c = null;
            try {
                c = Class.forName(cn, false, loader);
            } catch (ClassNotFoundException x) {
                fail(service,
                     "Provider " + cn + " not found");
            }
            if (!service.isAssignableFrom(c)) {
                fail(service,
                     "Provider " + cn  + " not a subtype");
            }
            try {
                S p = service.cast(c.newInstance());
                providers.put(cn, p);
                return p;
            } catch (Throwable x) {
                fail(service,
                     "Provider " + cn + " could not be instantiated",
                     x);
            }
            throw new Error();          // This cannot happen
        }

        public boolean hasNext() {
            if (acc == null) {
                return hasNextService();
            } else {
                PrivilegedAction<Boolean> action = new PrivilegedAction<Boolean>() {
                    public Boolean run() { return hasNextService(); }
                };
                return AccessController.doPrivileged(action, acc);
            }
        }

        public S next() {
            if (acc == null) {
                return nextService();
            } else {
                PrivilegedAction<S> action = new PrivilegedAction<S>() {
                    public S run() { return nextService(); }
                };
                return AccessController.doPrivileged(action, acc);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     *
     *  <p>延迟加载此加载程序服务的可用提供程序。
     *  <p>此方法返回的迭代器首先以实例化顺序生成提供程序缓存的所有元素。
     *  然后，它会延迟加载并实例化任何剩余的提供程序，然后将每个提供程序依次添加到缓存中。
     *
     *  <p>为了懒加载，解析可用的提供程序配置文件和实例化提供程序的实际工作必须由迭代器本身完成。
     *  因此，如果提供者配置文件违反指定的格式，或者如果它命名了无法找到和实例化的提供者类，
     *  或者实例化类的结果无法分配给服务类型，则其hasNext和next方法会引发ServiceConfigurationError。，
     *  或者在定位并实例化下一个提供程序时引发任何其他类型的异常或错误。要编写健壮的代码，
     *  仅在使用服务迭代器时才需要捕获ServiceConfigurationError。
     *
     *  <p>如果抛出这样的错误，
     *  则迭代器的后续调用将尽最大努力查找并实例化下一个可用的提供程序，但通常不能保证这种恢复。
     *
     *  <p>设计说明在这些情况下引发错误可能看起来很极端。
     *  此行为的原理是，格式错误的提供者配置文件（例如格式错误的类文件）
     *  指示Java虚拟机的配置或使用方式存在严重问题。
     *  因此，最好抛出一个错误而不是尝试恢复，或者更糟糕的是，静默地失败。
     *
     * <p> 此方法返回的迭代器不支持删除。调用其remove方法将导致引发UnsupportedOperationException。
     *
     * @implNote When adding providers to the cache, the {@link #iterator
     * Iterator} processes resources in the order that the {@link
     * java.lang.ClassLoader#getResources(java.lang.String)
     * ClassLoader.getResources(String)} method finds the service configuration
     * files.
     *
     * @return  An iterator that lazily loads providers for this loader's
     *          service
     */
    public Iterator<S> iterator() {
        return new Iterator<S>() {

            Iterator<Map.Entry<String,S>> knownProviders
                = providers.entrySet().iterator();

            public boolean hasNext() {
                if (knownProviders.hasNext())
                    return true;
                return lookupIterator.hasNext();
            }

            public S next() {
                if (knownProviders.hasNext())
                    return knownProviders.next().getValue();
                return lookupIterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * Creates a new service loader for the given service type and class
     * loader.
     *
     * @param  <S> the class of the service type
     *
     * @param  service
     *         The interface or abstract class representing the service
     *
     * @param  loader
     *         The class loader to be used to load provider-configuration files
     *         and provider classes, or <tt>null</tt> if the system class
     *         loader (or, failing that, the bootstrap class loader) is to be
     *         used
     *
     * @return A new service loader
     */
    public static <S> ServiceLoader<S> load(Class<S> service,
                                            ClassLoader loader)
    {
        return new ServiceLoader<>(service, loader);
    }

    /**
     * Creates a new service loader for the given service type, using the
     * current thread's {@linkplain java.lang.Thread#getContextClassLoader
     * context class loader}.
     *
     * <p> An invocation of this convenience method of the form
     *
     * <blockquote><pre>
     * ServiceLoader.load(<i>service</i>)</pre></blockquote>
     *
     * is equivalent to
     *
     * <blockquote><pre>
     * ServiceLoader.load(<i>service</i>,
     *                    Thread.currentThread().getContextClassLoader())</pre></blockquote>
     *
     * @param  <S> the class of the service type
     *
     * @param  service
     *         The interface or abstract class representing the service
     *
     * @return A new service loader
     */
    public static <S> ServiceLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ServiceLoader.load(service, cl);
    }

    /**
     * Creates a new service loader for the given service type, using the
     * extension class loader.
     *
     * <p> This convenience method simply locates the extension class loader,
     * call it <tt><i>extClassLoader</i></tt>, and then returns
     *
     * <blockquote><pre>
     * ServiceLoader.load(<i>service</i>, <i>extClassLoader</i>)</pre></blockquote>
     *
     * <p> If the extension class loader cannot be found then the system class
     * loader is used; if there is no system class loader then the bootstrap
     * class loader is used.
     *
     * <p> This method is intended for use when only installed providers are
     * desired.  The resulting service will only find and load providers that
     * have been installed into the current Java virtual machine; providers on
     * the application's class path will be ignored.
     *
     * @param  <S> the class of the service type
     *
     * @param  service
     *         The interface or abstract class representing the service
     *
     * @return A new service loader
     */
    public static <S> ServiceLoader<S> loadInstalled(Class<S> service) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        ClassLoader prev = null;
        while (cl != null) {
            prev = cl;
            cl = cl.getParent();
        }
        return ServiceLoader.load(service, prev);
    }

    /**
     * Returns a string describing this service.
     *
     * @return  A descriptive string
     */
    public String toString() {
        return "java.util.ServiceLoader[" + service.getName() + "]";
    }

}
