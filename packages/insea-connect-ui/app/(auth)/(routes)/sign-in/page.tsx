"use client";
import Image from "next/image";
import * as z from "zod";
import axios from "axios";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { signIn, useSession } from "next-auth/react";
import { useToast } from "@/components/ui/use-toast";
import { useRouter } from "next/navigation";

const passwordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

const formSchema = z.object({
  username: z.string().min(3, "Username must be at least 3 characters long."),
  // password: z.string().regex(passwordRegex, {
  //   message:
  //     "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.",
  // }),

  password: z.string().min(3, "Password must be at least 3 characters long."),
});

const SignInPage = () => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
  });

  const router = useRouter();

  const { toast } = useToast();

  const { isSubmitting, isValid } = form.formState;
  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    const result = await signIn("credentials", {
      username: values.username,
      password: values.password,
      redirect: false,
    });

    if (result?.error) {
      // TODO: Find a better way to handle incoming errors
      toast({
        title: "Invalid username or password",
        description:
          "If the problem persists, Please contact the administrator.",
        variant: "destructive",
      });
    } else {
      toast({
        title: "Welcome back!",
        description: "You have successfully signed in.",
      });
      router.push("/chat");
    }
  };
  return (
    <main className="w-full lg:grid lg:h-full lg:grid-cols-2">
      <div className="flex items-center justify-center py-12 lg:py-0">
        <div className="mx-auto grid gap-6 w-[350px] lg:w-[400px]">
          <div className="grid gap-2 text-center place-content-center">
            <Image
              className="mx-auto"
              src={"/logo.svg"}
              alt="insea connect logo"
              width={32}
              height={40}
            />
            <div>
              <h2 className="text-3xl/9 font-semibold tracking-[-75%]">
                INSEA Connect
              </h2>
              <p>Welcome back! Please enter your details.</p>
            </div>
          </div>
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="space-y-4 w-full"
            >
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Username</FormLabel>
                    <FormControl>
                      <Input
                        disabled={isSubmitting}
                        placeholder="Username"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              ></FormField>
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <Input
                        disabled={isSubmitting}
                        placeholder="Password"
                        type="password"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              ></FormField>
              <Button
                className="w-full"
                type="submit"
                disabled={!isValid || isSubmitting}
              >
                Sign In
              </Button>
            </form>
          </Form>
          <div className="mt-4 text-center text-sm">
            Don&apos;t have an account?{" "}
            <Link href="#" className="underline">
              Contact administrator
            </Link>
          </div>
        </div>
      </div>
      <div className="w-full bg-[url('/codioful-background.jpg')] bg-cover  bg-no-repeat "></div>
    </main>
  );
};

export default SignInPage;
