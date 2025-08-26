import { DefaultTheme } from "vitepress";

export const sidebar: DefaultTheme.Config["sidebar"] = {
  "/vongdefu-sourcecode-hub": [
    {
      text: "分布式事务",
      base: "/seata",
      items: [
        { text: "seata的安装", link: "/seata的安装" },
        { text: "分布式事务tcc模式", link: "/分布式事务tcc模式" },
      ],
    },
  ],
};
