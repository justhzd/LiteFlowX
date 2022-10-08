package top.xystudio.plugin.idea.liteflowx.util;

import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.xml.DomManager;
import top.xystudio.plugin.idea.liteflowx.dom.modal.Chain;
import top.xystudio.plugin.idea.liteflowx.dom.modal.Flow;
import top.xystudio.plugin.idea.liteflowx.dom.modal.Node;

public class XmlUtils {

    private static final String ROOT_TYPES = Flow.class.getSimpleName().toLowerCase();

    private static final ImmutableSet<String> TARGET_TYPES = ImmutableSet.of(
            Chain.class.getSimpleName().toLowerCase(),
            Node.class.getSimpleName().toLowerCase()
    );

    public static <T> T transformToDomElement(PsiElement element, Class<T> c){
        return (T) DomManager.getDomManager(element.getProject()).getDomElement((XmlTag) element);
    }

//    public static <T extends DomElement> List<DomFileElement<T>> findDomElements(@NotNull Project project, Class<T> clazz){
//        GlobalSearchScope globalSearchScope = GlobalSearchScope.allScope(project);
//        List<DomFileElement<T>> elements = DomService.getInstance().getFileElements(clazz, project, globalSearchScope);
//        return elements;
//    }

//    public static XmlTag getChainByName(Project project, String cmpName){
//        List<DomFileElement<Flow>> flows = findDomElements(project, Flow.class);
//        for (DomFileElement<Flow> flow : flows) {
//            for (Chain chain : flow.getRootElement().getChains()) {
//                if (chain.getName().getStringValue().equals(cmpName)){
//                    return chain.getXmlTag();
//                }
//            }
//        }
//        return null;
//    }

    public static boolean isLiteFlowXmlFile(PsiFile psiFile){
        if (psiFile == null){
            return false;
        }
        if (!(psiFile instanceof XmlFile)){
            return false;
        }
        XmlTag rootTag = ((XmlFile) psiFile).getRootTag();
        if (rootTag == null){
            return false;
        }
        if (!ROOT_TYPES.equals(rootTag.getName())){
            return false;
        }
        return true;
    }

    public static boolean isLiteflowXmlTarget(PsiElement psiElement){
        if (!(psiElement instanceof XmlToken)){
            return false;
        }
        XmlToken token = (XmlToken) psiElement;
        if (TARGET_TYPES.contains(token.getText())){
            PsiElement parent = token.getParent();
            if (parent instanceof XmlTag){
                PsiElement nextSibling = token.getNextSibling();
                if (nextSibling instanceof PsiWhiteSpace){
                    return true;
                }
            }
        }
        return false;
    }
}
